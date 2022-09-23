package com.lqk.activity.gl

import android.opengl.GLES32
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @author LQK
 * @time 2022/4/9 13:21
 * 三角形
 */

const val COORDS_PER_VERTEX = 3

// 三个点的位置
var triangleCoords = floatArrayOf(     // in counterclockwise order:
    0.0f, 0.622008459f, 0.0f,      // top
    -0.5f, -0.311004243f, 0.0f,    // bottom left
    0.5f, -0.311004243f, 0.0f      // bottom right
)

class Triangle {
    // 定义颜色 RGBA
    val color = floatArrayOf(0f, 0f, 0f, 1.0f)

    // 顶点着色器 代码
//    private val vertexShaderCode =
//        "attribute vec4 vPosition;" +
//                "void main(){" +
//                "   gl_Position = vPosition;" +
//                "}"
    private var vPMatrixHandle = 0
    private val vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main(){" +
                "   gl_Position = uMVPMatrix * vPosition;" +
                "}"

    // 片段着色器代码
    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main(){" +
                "   gl_FragColor = vColor;" +
                "}"

    private var mProgram: Int

    init {
        val vertexShader = loadShader(GLES32.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES32.GL_FRAGMENT_SHADER, fragmentShaderCode)
        mProgram = GLES32.glCreateProgram()
            .also { program ->
                // 添加 vertex shader 到 程序
                GLES32.glAttachShader(program, vertexShader)
                // 添加 fragment shader 到 程序
                GLES32.glAttachShader(program, fragmentShader)
                // 创建 OpenGL ES 程序 可执行文件
                GLES32.glLinkProgram(program)
            }
    }

    private var vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        .run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                // 添加 coords
                put(triangleCoords)
                // 设置 buffer
                position(0)
            }
        }

    /**
     * 加载 Shader
     */
    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES32.glCreateShader(type).also { shader ->
            GLES32.glShaderSource(shader, shaderCode)
            GLES32.glCompileShader(shader)
        }
    }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount = triangleCoords.size
    private val vertexStride = COORDS_PER_VERTEX * 4

    fun draw(mvpMatrix: FloatArray) {
        // 添加程序到 OpenGL ES 环境
        GLES32.glUseProgram(mProgram)
        // 绘制这个三角形
//        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vertexCount)

        // 设置 顶点着色器的 vPosition 成员
        positionHandle = GLES32.glGetAttribLocation(mProgram, "vPosition")
            .also {


                // 三角形
                GLES32.glEnableVertexAttribArray(it)
                //
                GLES32.glVertexAttribPointer(
                    it,
                    COORDS_PER_VERTEX,
                    GLES32.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
                )

                // 设置投影
                vPMatrixHandle = GLES32.glGetUniformLocation(mProgram, "uMVPMatrix")
                    .also { pMatrixHandle ->
                        // 通过 投影 与 视图变换 显示
                        GLES32.glUniformMatrix4fv(pMatrixHandle, 1, false, mvpMatrix, 0)
                    }

                // 设置 片段着色器的 vColor 成员
                mColorHandle = GLES32.glGetUniformLocation(mProgram, "vColor")
                    .also { colorHandle ->
                        // 设置绘制三角形的颜色
                        GLES32.glUniform4fv(colorHandle, 1, color, 0)
                    }
                // 绘制 这个三角形
                // vertexCount 点的数量
                GLES32.glDrawArrays(GLES32.GL_POINTS, 0, vertexCount)

                // 清除顶点
                GLES32.glDisableVertexAttribArray(it)
            }
    }
}