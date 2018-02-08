package com.aicyber.gathervoice.control

import android.util.Log
import okhttp3.*
import org.json.JSONObject
import org.json.JSONException
import java.io.*
import okhttp3.OkHttpClient
import okhttp3.RequestBody
//import org.apache.http.client.ClientProtocolException
//import org.apache.http.client.methods.HttpGet

//import org.apache.http.client.methods.HttpPost
//import org.apache.http.entity.StringEntity
//import org.apache.http.impl.client.DefaultHttpClient
//import org.apache.http.protocol.HTTP
//import org.apache.http.util.EntityUtils

@Suppress("DEPRECATION")
/**
 * Created by donar on 2017/11/22.
 */
class httpControlFunc private constructor(){
    companion object {
        val instance = httpControlFunc()
        var isSucceed = false
    }

    fun patch(url:String,para:JSONObject,tokenCode: String?):String
    {
        return post(url,para,tokenCode,true)
    }
    fun post(url:String,para:JSONObject,tokenCode: String?,isPatch:Boolean = false):String
    {
        isSucceed = false
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, para.toString())
        var result = ""

        try {
            val okHttpClient = OkHttpClient()
            var request:Request? = null
            if(!tokenCode.isNullOrEmpty()){
                if(!isPatch) {
                    request = Request.Builder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization", "Token " + tokenCode)
                            .url(url)
                            .post(body)
                            .build()
                }
                else
                {
                    request = Request.Builder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization", "Token " + tokenCode)
                            .url(url)
                            .patch(body)
                            .build()

                }
            }
            else
            {
                request = Request.Builder()
                        .addHeader("Content-Type", "application/json")
                        .url(url)
                        .post(body)
                        .build()
            }

            val call = okHttpClient.newCall(request)
            val response = call.execute()
            if(response.isSuccessful) {
                result = response.body().string()
                isSucceed = true
            }

        }
        catch (e:Exception)
        {
            println(e.message)
        }
        return result
    }
/*
    fun excPost(url:String,para:JSONObject,tokenCode: String?):String
    {
        isSucceed = false
        val httpRequst = HttpPost(url)
        httpRequst.setHeader("Content-Type", "application/json")
        if(!tokenCode.isNullOrEmpty())
            httpRequst.setHeader("Authorization", "Token " + tokenCode)
        val entity: StringEntity = StringEntity(para.toString(), HTTP.UTF_8)
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
*/
    fun get(url: String,tokenCode: String?):String
    {
        isSucceed = false
        var ret=""
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
                .addHeader("Content-Type", "application/json")
                .header("Authorization","Token "+tokenCode)
                .url(url)
                .build()
        val call = okHttpClient.newCall(request)
        try {
            val response = call.execute()
            println("ok http get execute")
            ret = response.body().string()
            if(response.isSuccessful) {
                println(ret)
                isSucceed = true
            }
        } catch ( e:Exception) {
            e.printStackTrace()
        }
        return ret
    }
/*
    fun getResultForHttpGet(url: String,tokenCode: String?):String? {
        try {
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
        catch (e:Exception)
        {
            print(e.message)
        }
        return null
    }
*/
    val TAG = "UploadHelper"

    fun uploadfile(url: String, item_id: Int, file: File,tokenCode: String?): String {
        isSucceed = false
        val fileBody = RequestBody.create(MediaType.parse("audio/wave"), file)   //application/octet-stream //audio/x-wav
        val type = MediaType.parse("application/json; charset=utf-8")
        var para:JSONObject = JSONObject().put("item_id",item_id)
        val body = RequestBody.create(type, para.toString())
        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //.addPart(body)
                .addFormDataPart("item_id",item_id.toString())
                .addFormDataPart("voice_file", file.name, fileBody)
                .build()

        val request = Request.Builder()
                //.addHeader("Connection", "close")
                .header("Authorization","Token "+tokenCode)
                .url(url)
                .patch(requestBody)
                .build()

        val response: Response
        try {
            var client = OkHttpClient()
            response = client.newCall(request).execute()
            val jsonString = response.body().string()
            Log.d(TAG, " upload jsonString =" + jsonString)

            if (!response.isSuccessful) {
                return jsonString
            } else {
                isSucceed = true
                return jsonString
            }

        } catch (e: IOException) {
            Log.d(TAG, "upload IOException ", e)
        } catch (e: JSONException) {
            Log.d(TAG, "upload JSONException ", e)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

}