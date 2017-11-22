package com.aicyber.gathervoice.control

import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import org.apache.http.*;

/**
 * Created by donar on 2017/11/22.
 */
class httpControlFunc {
    fun sendPostMessage(url:URL,params: Map<String, String>?,
                        encode: String): String
    {
        val buffer = StringBuffer()
        if (params != null && !params.isEmpty()) {
            for ((key, value) in params) {
                try {
                    buffer.append(key)
                            .append("=")
                            .append(URLEncoder.encode(value, encode))
                            .append("&")//请求的参数之间使用&分割。
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }

            }
            buffer.deleteCharAt(buffer.length - 1)
            println(buffer.toString())
            try {
                val urlConnection = url
                        .openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 3000
                //设置允许输入输出
                urlConnection.doInput = true
                urlConnection.doOutput = true
                val mydata = buffer.toString().toByteArray()
                //设置请求报文头，设定请求数据类型
                urlConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded")
                //设置请求数据长度
                urlConnection.setRequestProperty("Content-Length",
                        mydata.size.toString())
                //设置POST方式请求数据
                urlConnection.requestMethod = "POST"
                val outputStream = urlConnection.outputStream
                outputStream.write(mydata)
                val responseCode = urlConnection.responseCode
                if (responseCode == 200) {
                    return changeInputStream(urlConnection.inputStream,
                            encode)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return ""
    }

    /**
     * 把服务端返回的输入流转换成字符串格式
     * @param inputStream 服务器返回的输入流
     * @param encode 编码格式
     * @return 解析后的字符串
     */
    private fun changeInputStream(inputStream: InputStream?,
                                  encode: String): String
    {
        val outputStream = ByteArrayOutputStream()
        val data = ByteArray(1024)
        var len = 0
        var result = ""
        if (inputStream != null) {
            try {
                while (len != -1) {
                    outputStream.write(data, 0, len)
                    len = inputStream.read(data)
                }
                val charset = Charset.forName(encode)
                result = String(outputStream.toByteArray(),charset )

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return result
    }
    fun excPost(url:String,para:JSONObject)
    {
        //HttpPost postRequest = HttpPost(url)
    }
}