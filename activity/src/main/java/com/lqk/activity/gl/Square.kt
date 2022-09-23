package com.lqk.activity.gl

import android.opengl.GLES32
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/**
 * @author LQK
 * @time 2022/4/9 13:31
 * 正方形
 */
// 每个顶点的坐标值, 在这数组
const val COORDS_PER_VERTEX_SQUARE = 3

// 点坐标
var squareCoords = floatArrayOf(
    -0.2f, 0.2f, 0.0f,      // top left
    -0.2f, -0.2f, 0.0f,     // bottom left
    0.2f, -0.2f, 0.0f,      // bottom right
    0.2f, 0.2f, 0.0f        // top right
)

class Square {
    // 绘制顺序
    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)

    // 颜色
    val color = floatArrayOf(1f, 0.5f, 0.5f, 1.0f)

    // 顶点缓冲区
    private val vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(squareCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(squareCoords)
                position(0)
            }
        }

    // 绘制列表缓冲区
    private val drawListBuffer: ShortBuffer =
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }

    // 顶点着色器代码
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

    // 可执行程序文件
    private var mProgram: Int

    init {
        val vertexShader = loadShader(GLES32.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES32.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // 创建空的 OpenGL ES 可执行文件程序
        mProgram = GLES32.glCreateProgram()
            .also { program ->
                // 顶点着色器
                GLES32.glAttachShader(program, vertexShader)
                // 片段着色器
                GLES32.glAttachShader(program, fragmentShader)
                // 关联程序
                GLES32.glLinkProgram(program)
            }
    }

    private var mPositionHandle: Int = 0
    private var mColorHandle: Int = 0

    //
    private var vPMatrixHandle: Int = 0

    private val vertexCount = squareCoords.size / COORDS_PER_VERTEX_SQUARE
    private val vertexStride = COORDS_PER_VERTEX_SQUARE * 4 // 一个顶点 4个字节

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES32.glCreateShader(type)
            .also { shader ->
                GLES32.glShaderSource(shader, shaderCode)
                GLES32.glCompileShader(shader)
            }
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES32.glUseProgram(mProgram)


        mPositionHandle = GLES32.glGetAttribLocation(mProgram, "vPosition")
            .also { positionHandle ->
                GLES32.glEnableVertexAttribArray(positionHandle)
                // gl 顶点指针
                GLES32.glVertexAttribPointer(
                    positionHandle,
                    COORDS_PER_VERTEX_SQUARE,
                    GLES32.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
                )

                // 颜色
                mColorHandle = GLES32.glGetUniformLocation(mProgram, "vColor")
                    .also { colorHandle ->
                        GLES32.glUniform4fv(colorHandle, 1, color, 0)
                    }
                // 相机 投影
                vPMatrixHandle = GLES32.glGetUniformLocation(mProgram, "uMVPMatrix")
                //
                GLES32.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

                GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, 0, vertexCount)
//                GLES32.glDrawBuffers(GLES32.GL_TRIANGLES, drawListBuffer)
                GLES32.glDisableVertexAttribArray(positionHandle)
            }

    }
}