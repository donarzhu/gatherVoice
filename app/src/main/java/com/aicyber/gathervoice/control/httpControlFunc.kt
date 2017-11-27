package com.aicyber.gathervoice.control

import org.json.JSONObject
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.ByteArrayBody
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.protocol.HTTP
import org.apache.http.util.EntityUtils
import java.io.*

@Suppress("DEPRECATION")
/**
 * Created by donar on 2017/11/22.
 */
class httpControlFunc private constructor(){
    companion object {
        val instance = httpControlFunc()
        var isSucceed = false
    }
    fun sendPostfile(url:String,params: Map<String, String>?,
                     tokenCode: String,filepath:String,httpKey:String): String
    {

        val postRequest = HttpPost(url)
        postRequest.setHeader("Content-Type", "multipart/form-data")
        postRequest.setHeader("Authorization", "Token " + tokenCode)
        val reqEntity =  MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
        if(params != null) {
            for (key in params!!.keys) {
                reqEntity.addPart(key, StringBody(params!![key]))
            }
        }
        try{
            var bos = ByteArrayOutputStream()
            val file = File(filepath)
            val fls = FileInputStream(file)
            val length = fls.available()
            val buffer = ByteArray(length)
            fls.read(buffer)
            fls.close()
            val bab = ByteArrayBody(buffer, filepath)
            reqEntity.addPart(httpKey, bab)
        }
        catch( e:Exception){
            reqEntity.addPart("file", StringBody("file error"));
        }
        var result:String = ""
        try {
            postRequest.entity = reqEntity;
            val httpResponse = DefaultHttpClient().execute(postRequest)
            if(httpResponse.statusLine.statusCode == 200)
            {
                val httpEntity = httpResponse.entity;
                result = EntityUtils.toString(httpEntity) //取出应答字符串
            }
        }
        catch ( e:UnsupportedEncodingException) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = e.message.toString()
        }
        catch ( e: ClientProtocolException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            result = e.message.toString()
        }
        catch ( e:IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            result = e.message.toString()
        }
        return result
    }

    fun excPost(url:String,para:JSONObject,tokenCode: String?):String
    {
        isSucceed = false
        val httpRequst = HttpPost(url)
        httpRequst.setHeader("Content-Type", "application/json")
        if(!tokenCode.isNullOrEmpty())
            httpRequst.setHeader("Authorization", "Token " + tokenCode)
        val entity:StringEntity = StringEntity(para.toString(), HTTP.UTF_8)
        var result:String = ""
        try {
            httpRequst.entity = entity
            val httpResponse = DefaultHttpClient().execute(httpRequst)
            if(httpResponse.statusLine.statusCode == 200)
            {
                isSucceed = true
                val httpEntity = httpResponse.entity;
                result = EntityUtils.toString(httpEntity,HTTP.UTF_8) //取出应答字符串
            }
        } catch ( e:UnsupportedEncodingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            result = e.message.toString();
        }
        catch ( e: ClientProtocolException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            result = e.message.toString()
        }
        catch ( e:IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            result = e.message.toString()
        }
        return result;
    }

    fun getResultForHttpGet(url: String,tokenCode: String?):String {
        isSucceed = false
        var result = ""
        var httpGet = HttpGet(url);//编者按：与HttpPost区别所在，这里是将参数在地址中传递
        httpGet.setHeader("Content-Type", "application/json")
        if(!tokenCode.isNullOrEmpty())
            httpGet.setHeader("Authorization", "Token " + tokenCode)
        var response = DefaultHttpClient().execute(httpGet)
        if (response.statusLine.statusCode == 200) {
            var entity = response.entity;
            result = EntityUtils.toString(entity, HTTP.UTF_8)
            isSucceed = true
        }
        return result;
    }
}