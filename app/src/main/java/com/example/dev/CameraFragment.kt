package com.example.dev

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.dev.databinding.FragmentCameraBinding
import com.jeremyliao.liveeventbus.LiveEventBus
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * 相机预览
 */
class CameraFragment : Fragment() {

    companion object {
        private const val FILENAME = "yyyyMMddHHmmss"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        /**
         * 用于创建时间戳文件的助手函数
         * @param baseFolder
         * @param format
         * @param extension
         */
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(baseFolder, SimpleDateFormat(format, Locale.US).format(System.currentTimeMillis()) + extension)

        /**
         * 获取输出文件夹
         */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists()) mediaDir else appContext.filesDir
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = FragmentCameraBinding.inflate(inflater)
        R.layout.fragment_camera
        return viewBinding.root
//        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    private lateinit var viewBinding: FragmentCameraBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 相机预览控件
        viewFinder = viewBinding.svCamera
        // 相机执行者
        cameraExecutor = Executors.newSingleThreadExecutor()
        // 拍照的 存储文件夹
        outputDirectory = getOutputDirectory(this.requireActivity())
        viewFinder.post {
            // 跟踪附着此视图的显示
            displayId = viewFinder.display.displayId
            // 生成UI控件
            updateCameraUi()
            // 设置摄像头及其使用案例
            setUpCamera()
        }
        initEvent()
    }

    private fun initEvent() {
        LiveEventBus.get<String>("KEY_SAVE").observe(this) {
            Toast.makeText(this@CameraFragment.requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 如果设备有可用的后置摄像头，则返回true。否则为假
     */
    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    /**
     * 如果设备有可用的前置摄像头，则返回true。否则为假
     */
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    /**
     * 初始化CameraX，并准备绑定相机用例
     */
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireActivity())
        cameraProviderFuture.addListener(Runnable {
            // CameraProvider
            cameraProvider = cameraProviderFuture.get()
            // 根据可用摄影机选择镜头对齐
            lensFacing = when {
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }
            // 构建并绑定摄像机用例
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this.requireContext()))
    }

    /**
     * 声明和绑定预览、捕获和分析用例
     */
    private fun bindCameraUseCases() {
        // 获取用于设置摄像机全屏分辨率的屏幕指标
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        // 获取预览比例
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = viewFinder.display.rotation
        // CameraProvider
        val cameraProvider = cameraProvider ?: throw IllegalStateException("Camera initialization failed.")
        // 相机选择器
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        // 预览
        preview = Preview.Builder()
            // 我们要求纵横比，但没有分辨率
            .setTargetAspectRatio(screenAspectRatio)
            // 初始旋转目标
            .setTargetRotation(rotation)
            .build()
        // 图像捕获
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            // 我们要求纵横比，但没有分辨率来匹配预览配置，但是
            // CameraX针对最适合我们用例的特定分辨率进行优化
            .setTargetAspectRatio(screenAspectRatio)
            // 设置初始目标旋转，如果旋转改变，我们将不得不再次调用此选项
            // 在这个用例的生命周期中
            .setTargetRotation(rotation)
            .build()
        // 图像分析
        imageAnalyzer = ImageAnalysis.Builder()
            // 我们要求纵横比，但没有分辨率
            .setTargetAspectRatio(screenAspectRatio)
            // 设置初始目标旋转，如果旋转改变，我们将不得不再次调用此选项
            // 在这个用例的生命周期中
            .setTargetRotation(rotation)
            .build()
            // 然后可以将分析器分配给实例
            .also {
                it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                    // 从分析器返回的值被传递到附加的侦听器
                    // 我们在这里记录图像分析结果-你应该做些有用的事情
                    // 取而代之的是！

                })
            }
        // 必须在重新绑定用例之前解除它们的绑定
        cameraProvider.unbindAll()
        try {
            // 可以在此处传递可变数量的用例CameraControl 和 CameraInfo
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture, imageAnalyzer
            )
            // 重新绘制相机UI控件附加取景器的表面提供者以预览用例
            preview?.setSurfaceProvider(viewFinder.surfaceProvider)
        } catch (exc: Exception) {

        }
    }

    private lateinit var viewFinder: PreviewView
    private lateinit var outputDirectory: File
    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    // 使用此执行器执行阻止摄像机操作
    private lateinit var cameraExecutor: ExecutorService

    /**
     * 用于重新绘制摄影机UI控件，每次配置更改时调用
     */
    private fun updateCameraUi() {
        // 用于捕获照片的按钮的侦听器 -> 拍照
        viewBinding.btnTakePhoto.setOnClickListener {
            // 获取可修改图像捕获用例的稳定参考
            imageCapture?.let { imageCapture ->
                // 创建保存图像的输出文件
                val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
                // 安装映像捕获元数据
                val metadata = ImageCapture.Metadata().apply {
                    // 使用前置摄像头时镜像
                    isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
                }
                // 创建包含文件+元数据的输出选项对象
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                    .setMetadata(metadata)
                    .build()

                // 设置在拍照后触发的图像捕捉侦听器
                imageCapture.takePicture(
                    outputOptions,
                    cameraExecutor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            Toast.makeText(this@CameraFragment.requireContext(), "${exc.message}", Toast.LENGTH_SHORT).show()
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            // 图片保存成功
                            val saveUri = output.savedUri ?: Uri.fromFile(photoFile)
                            LiveEventBus.get<String>("KEY_SAVE").post(saveUri.path)
                        }
                    })
            }
        }
    }

    /**
     * 通过计算绝对值来检测@params中提供的尺寸的最合适比率的预览比率
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    /**
     * 自定义图像分析类
     * 我们需要做的就是用我们想要的操作覆盖函数 analyze。
     * 在这里 我们通过观察YUV框架的Y平面来计算图像的平均亮度
     */
    private class LuminosityAnalyzer(listener: LumaListener? = null) : ImageAnalysis.Analyzer {
        private val frameRateWindow = 8
        private val frameTimestamps = ArrayDeque<Long>(5)
        private val listeners = ArrayList<LumaListener>().apply { listener?.let { add(it) } }
        private var lastAnalyzedTimestamp = 0L
        var framesPerSecond: Double = -1.0
            private set

        /**
         * 用于添加将在计算每个 luma 时调用的侦听器
         */
        fun onFrameAnalyzed(listener: LumaListener) = listeners.add(listener)

        /**
         * 用于从图像平面缓冲区提取字节数组的助手扩展函数
         */
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // 将缓冲区倒回零
            val data = ByteArray(remaining())
            get(data)   // 将缓冲区复制到字节数组中
            return data // 返回字节数组
        }

        /**
         * 分析图像以生成结果。
         * 调用者负责确保快速执行此分析方法
         * 足以防止图像采集管道中的暂停。否则，新提供不会采集和分析图像。
         * 传递给此方法的图像在该方法返回后无效。来电者
         * 不应存储对此映像的外部引用，因为这些引用将无效。
         *
         * @param image 图像分析非常重要：分析方法的实现必须
         * 调用 image.close() 完成使用后收到的图像。
         * 否则，新图像根据背压设置，可能无法接收或相机可能停止。
         */
        override fun analyze(image: ImageProxy) {
            // 如果没有附加的侦听器，我们就不需要执行分析
            if (listeners.isEmpty()) {
                image.close()
                return
            }
            // 跟踪分析的帧
            val currentTime = System.currentTimeMillis()
            frameTimestamps.push(currentTime)
            // 使用移动平均值计算FPS
            while (frameTimestamps.size >= frameRateWindow) frameTimestamps.removeLast()
            val timestampFirst = frameTimestamps.peekFirst() ?: currentTime
            val timestampLast = frameTimestamps.peekLast() ?: currentTime
            framesPerSecond = 1.0 / ((timestampFirst - timestampLast) / frameTimestamps.size.coerceAtLeast(1).toDouble()) * 1000.0
            // 分析可能需要任意长的时间
            // 因为我们在不同的线程中运行，所以它不会暂停其他用例
            lastAnalyzedTimestamp = frameTimestamps.first
            // 因为图像分析的格式是 YUV, image.planes[0] ]包含亮度平面
            val buffer = image.planes[0].buffer
            // 从回调对象提取图像数据
            val data = buffer.toByteArray()
            // 将数据转换为像素值数组 0-255
            val pixels = data.map { it.toInt() and 0xFF }
            // 计算图像的平均亮度
            val luma = pixels.average()
            // 用新值调用所有侦听器
            listeners.forEach { it(luma) }

            image.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

typealias LumaListener = (luma: Double) -> Unit