package com.aicyber.gathervoice.control

import android.os.Handler
import android.os.Message
import com.google.gson.Gson
import org.json.JSONObject
import kotlin.concurrent.thread

/**
 * Created by donar on 2017/11/23.
 */
class global {
    companion object {
        val openDebugData = true
        val serverUri = "http://101.200.142.165:8010"
        var token:String? = null
        var username:String = ""
        var id:Int = 0
        private fun cmdVerifyCode_post():String = serverUri + "/api/user/sms/verify-code/"
        private fun cmdRegister_post():String = serverUri + "/api/user/register/"
        private fun cmdLogin_post():String = serverUri + "/api/user/login/"
        private fun cmdVoiceTaskList_get():String = serverUri + "/api/task/_app/can_todo/"
        private fun cmdVerifyTaskList_get():String = serverUri + "/api/task/_app/todo/can_verify/"
        private fun cmdMyTaskList_get():String = serverUri +"/api/task/_app/my-todo/"
        private fun cmdMyVerifyList_get():String = serverUri +"/api/task/_app/my-verify/"

        fun verifyCode(handler: Handler,phone:String) {
            var thread = thread {
                kotlin.run {
                    var uri = cmdVerifyCode_post()
                    var para:JSONObject = JSONObject().put("tel",phone)
                    var ret = httpControlFunc.instance.excPost(uri,para,null)
                    if(!ret.isNullOrEmpty())
                    {
                        var msg = Message()
                        msg.data.putString("info",ret)
                        if(httpControlFunc.isSucceed)
                            msg.what=1
                        else
                            msg.what=2
                        handler.sendMessageAtTime(msg,10000)
                    }
                }
            }
            thread.start()
        }

        fun register(handler: Handler,phone:String,password:String,code:String)
        {
            var theard = Thread{
                kotlin.run {
                    val uri = cmdRegister_post()
                    var para:JSONObject = JSONObject().put("password",password)
                    para.put("username",phone)
                    para.put("code",code)
                    var ret = httpControlFunc.instance.excPost(uri,para,null)
                    if(!ret.isNullOrEmpty())
                    {
                        var msg = Message()
                        msg.data.putString("info",ret)
                        if(httpControlFunc.isSucceed)
                            msg.what=3
                        else
                            msg.what=4
                        val regInfo:registerResult = Gson().fromJson(ret, registerResult::class.java)
                        token = regInfo.token
                        username = regInfo.username
                        id=regInfo.id
                        handler.sendMessageAtTime(msg,10000)
                    }
                }
            }
            theard.start()
        }
        fun login(handler: Handler,phone:String,password:String)
        {
            var theard = Thread{
                kotlin.run {
                    val uri = cmdLogin_post()
                    var para:JSONObject = JSONObject().put("password",password)
                    para.put("username",phone)
                    var ret = httpControlFunc.instance.excPost(uri,para,null)
                    var msg = Message()
                    if(!ret.isNullOrEmpty())
                    {
                        msg.data.putString("info",ret)
                        if(httpControlFunc.isSucceed)
                            msg.what=5
                        else
                            msg.what=6
                        val regInfo:loginResult = Gson().fromJson(ret, loginResult::class.java)
                        token = regInfo.auth_token
                        username = regInfo.username
                        id=regInfo.id
                    }
                    else
                    {
                        msg.what = 6
                    }
                    handler.sendMessageAtTime(msg,10000)
                }
            }
            theard.start()
        }

        fun getVoiceTaskList(handler: Handler)
        {
            val thread = Thread{
                kotlin.run {
                    val uri = cmdVoiceTaskList_get()
                    var ret = httpControlFunc.instance.getResultForHttpGet(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 7
                            handler.sendMessageAtTime(msg, 10000)
                        }
                    }
                }
            }
            thread.start()
        }

        fun getVerifyTaskList(handler: Handler)
        {
            val thread = Thread{
                kotlin.run {
                    val uri = cmdVerifyTaskList_get()
                    var ret = httpControlFunc.instance.getResultForHttpGet(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 8
                            handler.sendMessageAtTime(msg, 10000)
                        }
                    }
                }
            }
            thread.start()
        }

        fun getMyTaskList(handler: Handler)
        {
            val thread = Thread{
                kotlin.run {
                    val uri = cmdMyTaskList_get()
                    var ret = httpControlFunc.instance.getResultForHttpGet(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 9
                            handler.sendMessageAtTime(msg, 10000)
                        }
                    }
                }
            }
            thread.start()
        }

        fun getMyVerifyList(handler: Handler)
        {
            val thread = Thread{
                kotlin.run {
                    val uri = cmdMyVerifyList_get()
                    var ret = httpControlFunc.instance.getResultForHttpGet(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 10
                            handler.sendMessageAtTime(msg, 10000)
                        }
                    }
                }
            }
            thread.start()
        }

    }


    inner class loginResult
    {
        var username:String = ""
        var is_superuser:Boolean = false
        var auth_token:String = ""
        var id:Int=0
    }
    inner class registerResult
    {
        var id:Int=0
        var username:String = ""
        var token:String = ""
    }
}