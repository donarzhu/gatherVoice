package com.aicyber.gathervoice.control

import android.os.Handler
import android.os.Message
import com.google.gson.Gson
import org.json.JSONObject
import java.io.File
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
        var sendFileHandler:Handler?=null
        var sendDataHandler:Handler?=null
        private fun cmdVerifyCode_post():String = serverUri + "/api/user/sms/verify-code/"
        private fun cmdRegister_post():String = serverUri + "/api/user/register/"
        private fun cmdLogin_post():String = serverUri + "/api/user/login/"
        private fun cmdVoiceTaskList_get():String = serverUri + "/api/task/_app/can_todo/"
        private fun cmdVerifyTaskList_get():String = serverUri + "/api/task/_app/todo/can_verify/"
        private fun cmdMyTaskList_get():String = serverUri +"/api/task/_app/my-todo/"
        private fun cmdMyVerifyList_get():String = serverUri +"/api/task/_app/my-verify/"
        private fun cmdSenVoiceItem_post():String = serverUri +"/api/task/_app/my-todo-item/"
        private fun cmdVoiceTaskItems_get(item_id: Int):String = serverUri + "/api/task/_app/my-todo/"+item_id.toString() +"/item/"
        private fun cmdTodoVoiceTask_get(task_id:Int) = serverUri + "/api/task/_app/"+task_id.toString()+"/todo/"
        private fun cmdToduVerityTask_get(task_id: Int)= serverUri+ "/api/task/_app/todo/"+task_id.toString()+"/verify/"
        private fun cmdVerifyItems_get(task_id:Int) = serverUri +"/api/task/_app/my-verify/"+task_id.toString()+"/item/"
        private fun cmdVerifyItem_patch():String= serverUri+"/api/task/_app/my-verify-item/"
        private fun cmdUpdateUserInfo_patch():String= serverUri+"/api/user/info/"
        private fun cmdGetUserInfo_get():String = serverUri + "/api/user/info/"
        private fun cmdMessage_get():String = serverUri +"/api/user/note/"

        fun verifyCode(handler: Handler,phone:String) {
           thread { //t大小写代表不同的运行方式
                kotlin.run {
                    var uri = cmdVerifyCode_post()
                    var para:JSONObject = JSONObject().put("tel",phone)
                    var ret = httpControlFunc.instance.post(uri,para,null)
                    if(!ret.isNullOrEmpty())
                    {
                        var msg = Message()
                        msg.data.putString("info",ret)
                        if(httpControlFunc.isSucceed)
                            msg.what=1
                        else
                            msg.what=2
                        handler.sendMessage(msg)
                    }
                }
            }
        }

        fun register(handler: Handler,phone:String,password:String,code:String)
        {
            thread{
                kotlin.run {
                    val uri = cmdRegister_post()
                    var para:JSONObject = JSONObject().put("password",password)
                    para.put("username",phone)
                    para.put("code",code)
                    var ret = httpControlFunc.instance.post(uri,para,null)
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
                        handler.sendMessage(msg)
                    }
                }
            }
        }
        fun login(handler: Handler,phone:String,password:String)
        {
            thread{
                kotlin.run {
                    val uri = cmdLogin_post()
                    var para:JSONObject = JSONObject().put("password",password)
                    para.put("username",phone)
                    var ret = httpControlFunc.instance.post(uri,para,null)
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
                    handler.sendMessage(msg)
                }
            }
        }

        fun getVoiceTaskList(handler: Handler)
        {
            thread{
                kotlin.run {
                    val uri = cmdVoiceTaskList_get()
                    var ret = httpControlFunc.instance.get(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 7
                            handler.sendMessage(msg)
                        }
                    }
                }
            }
        }

        fun getVerifyTaskList(handler: Handler)
        {
            thread{
                kotlin.run {
                    val uri = cmdVerifyTaskList_get()
                    var ret = httpControlFunc.instance.get(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 8
                            handler.sendMessage(msg)
                        }
                    }
                }
            }
        }

        fun getMyTaskList(handler: Handler)
        {
            thread{
                kotlin.run {
                    val uri = cmdMyTaskList_get()
                    var ret = httpControlFunc.instance.get(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 9
                            handler.sendMessage(msg)
                        }
                    }
                }
            }
        }

        fun getMyVerifyList(handler: Handler)
        {
            thread{
                kotlin.run {
                    val uri = cmdMyVerifyList_get()
                    var ret = httpControlFunc.instance.get(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 10
                            handler.sendMessage(msg)
                        }
                    }
                }
            }
        }
        fun sendVoiceFile(handler: Handler,item_id:Int,file:String)
        {
            thread{
                kotlin.run {
                    val uri = cmdSenVoiceItem_post()
                    val _file = File(file)
                    //var ret = httpControlFunc.instance.uploadfile(uri,item_id, _file, token)
                    var param = mapOf(Pair("item_id",item_id.toString()))
                    var ret = httpControlFunc.instance.uploadfile(uri,item_id,_file, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 11
                        }
                        else
                        {
                            msg.what = 12
                        }
                        handler.sendMessage(msg)
                    }
                }
            }
        }
        fun getVoiceTaskItems(handler: Handler,item_id:Int)
        {
            thread{
                kotlin.run {
                    val uri = cmdVoiceTaskItems_get(item_id)
                    var ret = httpControlFunc.instance.get(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 13
                            handler.sendMessage(msg)
                        }
                    }
                }
            }
        }
        fun todoVoiceTask(handler: Handler,task_id: Int)
        {
            thread{
                kotlin.run {
                    val uri = cmdTodoVoiceTask_get(task_id)
                    var ret = httpControlFunc.instance.get(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 15
                        }
                        else
                        {
                            msg.what = 16
                        }
                        handler.sendMessage(msg)
                    }
                }
            }
        }

        fun todoVerityTask(handler: Handler,task_id: Int)
        {
            thread{
                kotlin.run {
                    val uri = cmdToduVerityTask_get(task_id)
                    var ret = httpControlFunc.instance.get(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 17
                        }
                        else
                        {
                            msg.what = 18
                        }
                        handler.sendMessage(msg)
                    }
                }
            }
        }

        fun getVerifyItems(handler: Handler,task_id:Int)
        {
            thread{
                kotlin.run {
                    val uri = cmdVerifyItems_get(task_id)
                    var ret = httpControlFunc.instance.get(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 19
                            handler.sendMessage(msg)
                        }
                    }
                }
            }
        }

        fun verifyItem(handler: Handler,item_id: Int,result:String,review:String?) {
            thread{
                kotlin.run {
                    var uri = cmdVerifyItem_patch()
                    var para:JSONObject = JSONObject().put("item_id",item_id)
                    para.put("result",result)
                    para.put("review",review)
                    var ret = httpControlFunc.instance.patch(uri,para, token)
                    if(!ret.isNullOrEmpty())
                    {
                        var msg = Message()
                        msg.data.putString("info",ret)
                        if(httpControlFunc.isSucceed)
                            msg.what=21
                        else
                            msg.what=22
                        handler.sendMessage(msg)
                    }
                }
            }
        }

        fun updateUserInfo(handler: Handler,sex: String,age:Int,place:String?) {
            thread{
                kotlin.run {
                    var uri = cmdUpdateUserInfo_patch()
                    var para:JSONObject = JSONObject().put("sex",sex)
                    para.put("age",age)
                    para.put("place",place)
                    var ret = httpControlFunc.instance.patch(uri,para, token)
                    if(!ret.isNullOrEmpty())
                    {
                        var msg = Message()
                        msg.data.putString("info",ret)
                        if(httpControlFunc.isSucceed)
                            msg.what=23
                        else
                            msg.what=24
                        handler.sendMessage(msg)
                    }
                }
            }
        }

        fun getUserInfo(handler: Handler)
        {
            thread{
                kotlin.run {
                    val uri = cmdGetUserInfo_get()
                    var ret = httpControlFunc.instance.get(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 25
                            handler.sendMessage(msg)
                        }
                    }
                }
            }
        }

        fun getMessages(handler: Handler)
        {
            thread{
                kotlin.run {
                    val uri = cmdMessage_get()
                    var ret = httpControlFunc.instance.get(uri, token)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info", ret)
                        if (httpControlFunc.isSucceed) {
                            msg.what = 27
                            handler.sendMessage(msg)
                        }
                    }
                }
            }
        }
 ////////////////////end
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