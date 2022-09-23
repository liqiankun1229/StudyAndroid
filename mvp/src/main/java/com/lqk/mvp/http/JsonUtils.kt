package com.lqk.mvp.http

/**
 * @author LQK
 * @time 2022/6/25 22:10
 *
 */
object JsonUtils {

    fun formatJson(jsonStr: String): String {
        if (jsonStr.isEmpty()) return ""
        val sb = StringBuilder()
        var last = '\u0000'
        var current = '\u0000'
        var indent = 0
        for (i in jsonStr.indices) {
            last = current
            current = jsonStr[i]
            when (current) {
                '{', '[' -> {
                    sb.append(current)
                    sb.append('\n')
                    indent++
                    addIndentBlank(sb, indent)
                }
                '}', ']' -> {
                    sb.append('\n')
                    indent--
                    addIndentBlank(sb, indent)
                    sb.append(current)
                }
                ',' -> {
                    sb.append(current)
                    if (last != '\\') {
                        sb.append('\n')
                        addIndentBlank(sb, indent)
                    }
                }
                else -> {
                    sb.append(current)
                }
            }
        }
        return sb.toString()
    }

    private fun addIndentBlank(stringBuilder: StringBuilder, indent: Int) {
        for (i in 0 until indent) {
            stringBuilder.append('\t')
        }
    }

    fun decodeUnicode(str: String): String {
        var aChar: Char
        var len = str.length
        var outBuffer = StringBuffer(len)
        var i = 0
        while (i < len) {
            aChar = str[i++]
            if (aChar == '\\') {
                aChar = str[i++]
                if (aChar == 'u') {
                    var v = 0
                    for (j in 0 until 4) {
                        aChar = str[i++]
                        when (aChar) {
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                                v = (v shl 4) + aChar.digitToInt() - '0'.digitToInt()
                            }
                            'a', 'b', 'c', 'd', 'e', 'f' -> {
                                v = (v shl 4) + 10 + aChar.digitToInt() - 'a'.digitToInt()
                            }
                            'A', 'B', 'C', 'D', 'E', 'F' -> {
                                v = (v shl 4) + 10 + aChar.digitToInt() - 'A'.digitToInt()
                            }
                            else -> {
                                throw IllegalArgumentException("Malformed   \\\\uxxxx   encoding.")
                            }
                        }
                    }
                    outBuffer.append(v.toChar())
                } else {
                    when {
                        aChar == 't' -> {
                            aChar = '\t'
                        }
                        aChar == 'r' -> {
                            aChar = '\r'
                        }
                        aChar == 'n' -> {
                            aChar = '\n'
                        }
                        aChar == 'f' -> {
                            aChar = 'f'
                        }
                        else -> {

                        }
                    }
                    outBuffer.append(aChar)
                }
            } else {
                outBuffer.append(aChar)
            }
        }
        return outBuffer.toString()
    }
}