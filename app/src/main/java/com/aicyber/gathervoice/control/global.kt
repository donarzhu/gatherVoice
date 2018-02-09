package com.aicyber.gathervoice.control

import android.os.Handler
import android.os.Message
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import kotlin.concurrent.thread

/**
 * Created by donar on 2017/11/23.
 */
class global {
    companion object {
        val openDebugData = true
        val serverUri = "http://101.200.142.165:8020"
        var token: String? = null
        var username: String = ""
        var id: Int = 0
        var sendFileHandler: Handler? = null
        var sendDataHandler: Handler? = null
        var SysVersion = ""
        private fun cmdVerifyCode_post(): String = serverUri + "/api/user/_app/sms/vcode/"
        private fun cmdRegister_post(): String = serverUri + "/api/user/register/"
        private fun cmdLogin_post(): String = serverUri + "/api/user/login/"
        private fun cmdVoiceTaskList_get(): String = serverUri + "/api/task/_app/can_todo/"
        private fun cmdVerifyTaskList_get(): String = serverUri + "/api/task/_app/todo/can_verify/"
        private fun cmdMyTaskList_get(): String = serverUri + "/api/task/_app/my-todo/"
        private fun cmdMyVerifyList_get(): String = serverUri + "/api/task/_app/my-verify/"
        private fun cmdSenVoiceItem_post(): String = serverUri + "/api/task/_app/my-todo-item/"
        private fun cmdVoiceTaskItems_get(item_id: Int): String = serverUri + "/api/task/_app/my-todo/" + item_id.toString() + "/item/"
        private fun cmdTodoVoiceTask_get(task_id: Int) = serverUri + "/api/task/_app/" + task_id.toString() + "/todo/"
        private fun cmdToduVerityTask_get(task_id: Int) = serverUri + "/api/task/_app/todo/" + task_id.toString() + "/verify/"
        private fun cmdVerifyItems_get(task_id: Int) = serverUri + "/api/task/_app/my-verify/" + task_id.toString() + "/item/"
        private fun cmdVerifyItem_patch(): String = serverUri + "/api/task/_app/my-verify-item/"
        private fun cmdUpdateUserInfo_patch(): String = serverUri + "/api/user/info/"
        private fun cmdGetUserInfo_get(): String = serverUri + "/api/user/info/"
        private fun cmdMessage_get(): String = serverUri + "/api/user/note/"
        private fun cmdRestPwd(): String = serverUri + "/api/user/_app/password/reset/"
        private fun cmdChangePwd(): String = serverUri + "/api/user/password/change/"
        private fun cmdWithdrawCash_post(): String = serverUri + "/api/user/_app/wage2/"
        private fun cmdCreateUserMessage_post():String = serverUri +"/api/user/issue/"

        private fun sendMessage(handler: Handler,ret:String,sCode:Int,fCode:Int)
        {
            var msg = Message()
            if (!ret.isNullOrEmpty()) {
                msg.data.putString("info" , ret)
                if (httpControlFunc.isSucceed)
                    msg.what = sCode
                else
                    msg.what = fCode
            }
            else
            {
                msg.what = -1
            }
            handler.sendMessage(msg)
        }
        fun userMessage(handler: Handler,message:String)
        {
            thread {
                kotlin.run {
                    var uri = cmdCreateUserMessage_post()
                    var para: JSONObject = JSONObject().put("t1" , message)
                    para.put("u2",0)

                    var ret = httpControlFunc.instance.post(uri , para , token)
                    sendMessage(handler,ret,39,40)
                }
            }
        }

        fun bindingHankCard(handler: Handler ,pwd:String, id_no: String , bank: String , card_no: String) {
            thread {
                kotlin.run {
                    var uri = cmdUpdateUserInfo_patch()
                    var para: JSONObject = JSONObject().put("password",pwd)
                    para.put("id_no" , id_no)
                    para.put("bank" , bank)
                    para.put("card_no" , card_no)
                    var ret = httpControlFunc.instance.patch(uri , para , token)
                    sendMessage(handler,ret,37,38)
                }
            }
        }

        fun withdrawCash(handler: Handler , money: Double) {
            thread {
                kotlin.run {
                    var uri = cmdWithdrawCash_post()
                    var para: JSONObject = JSONObject().put("money_str" , money.toString())
                    var ret = httpControlFunc.instance.post(uri , para , token)
                    sendMessage(handler,ret,35,36)
                }
            }
        }

        fun changePwd(handler: Handler , oldPwd: String , newPwd: String) {
            thread {
                kotlin.run {
                    var uri = cmdChangePwd()
                    var para: JSONObject = JSONObject().put("current_password" , oldPwd)
                    para.put("new_password" , newPwd)
                    var ret = httpControlFunc.instance.post(uri , para , token)
                    sendMessage(handler,ret,33,34)
                }
            }

        }

        fun resetPwd(handler: Handler , tel:String,newPwd: String , code: String) {
            thread {
                kotlin.run {
                    var uri = cmdRestPwd()
                    var para: JSONObject = JSONObject().put("tel",tel)
                    para.put("new_password" , newPwd)
                    para.put("code" , code)
                    var ret = httpControlFunc.instance.post(uri , para , null)
                    sendMessage(handler,ret,31,32)
                }
            }
        }

        fun verifyCode(handler: Handler , phone: String) {
            thread {
                //t大小写代表不同的运行方式
                kotlin.run {
                    var uri = cmdVerifyCode_post()
                    var para: JSONObject = JSONObject().put("tel" , phone)
                    var ret = httpControlFunc.instance.post(uri , para , null)
                    sendMessage(handler,ret,1,2)
                }
            }
        }

        fun register(handler: Handler , phone: String , password: String , age: Int , sex: Int , code: String , dialect: Array<Int> , user: String) {
            thread {
                kotlin.run {
                    val uri = cmdRegister_post()
                    var para: JSONObject = JSONObject().put("username" , phone)
                    para.put("password" ,password )
                    para.put("age" , age)
                    para.put("sex" , sex)
                    para.put("code" , code)
                    var jsonArray = JSONArray()
                    for(value in dialect)
                    {
                        jsonArray.put(value)
                    }
                    para.put("dialect" , jsonArray)
                    para.put("fullname" , user)
                    var ret = httpControlFunc.instance.post(uri , para , null)
                    if (!ret.isNullOrEmpty()) {
                        var msg = Message()
                        msg.data.putString("info" , ret)
                        if (httpControlFunc.isSucceed)
                            msg.what = 3
                        else
                            msg.what = 4
                        val regInfo: registerResult = Gson().fromJson(ret , registerResult::class.java)
                        token = regInfo.token
                        username = regInfo.username
                        id = regInfo.id
                        handler.sendMessage(msg)
                    }
                }
            }
        }

        fun login(handler: Handler , phone: String , password: String) {
            thread {
                kotlin.run {
                    val uri = cmdLogin_post()
                    var para: JSONObject = JSONObject().put("password" , password)
                    para.put("username" , phone)
                    var ret = httpControlFunc.instance.post(uri , para , null)
                    var msg = Message()
                    if (!ret.isNullOrEmpty()) {
                        msg.data.putString("info" , ret)
                        if (httpControlFunc.isSucceed)
                            msg.what = 5
                        else
                            msg.what = 6
                        val regInfo: loginResult = Gson().fromJson(ret , loginResult::class.java)
                        token = regInfo.auth_token
                        username = regInfo.username
                        id = regInfo.id
                    } else {
                        msg.what = 6
                    }
                    handler.sendMessage(msg)
                }
            }
        }

        fun getVoiceTaskList(handler: Handler) {
            thread {
                kotlin.run {
                    val uri = cmdVoiceTaskList_get()
                    var ret = httpControlFunc.instance.get(uri , token)
                    var msg = Message()
                    sendMessage(handler,ret,7,-1)
                }
            }
        }

        fun getVerifyTaskList(handler: Handler) {
            thread {
                kotlin.run {
                    val uri = cmdVerifyTaskList_get()
                    var ret = httpControlFunc.instance.get(uri , token)
                    var msg = Message()
                    sendMessage(handler,ret,8,-1)
                }
            }
        }

        fun getMyTaskList(handler: Handler) {
            thread {
                kotlin.run {
                    val uri = cmdMyTaskList_get()
                    var ret = httpControlFunc.instance.get(uri , token)
                    var msg = Message()
                    sendMessage(handler,ret,9,-1)
                }
            }
        }

        fun getMyVerifyList(handler: Handler) {
            thread {
                kotlin.run {
                    val uri = cmdMyVerifyList_get()
                    var ret = httpControlFunc.instance.get(uri , token)
                    var msg = Message()
                    sendMessage(handler,ret,10,-1)
                }
            }
        }

        fun sendVoiceFile(handler: Handler , item_id: Int , file: String) {
            thread {
                kotlin.run {
                    val uri = cmdSenVoiceItem_post()
                    val _file = File(file)
                    //var ret = httpControlFunc.instance.uploadfile(uri,item_id, _file, token)
                    var param = mapOf(Pair("item_id" , item_id.toString()))
                    var ret = httpControlFunc.instance.uploadfile(uri , item_id , _file , token)
                    var msg = Message()
                    sendMessage(handler,ret,11,12)
                }
            }
        }

        fun getVoiceTaskItems(handler: Handler , item_id: Int) {
            thread {
                kotlin.run {
                    val uri = cmdVoiceTaskItems_get(item_id)
                    var ret = httpControlFunc.instance.get(uri , token)
                    var msg = Message()
                    sendMessage(handler,ret,13,-1)
                }
            }
        }

        fun todoVoiceTask(handler: Handler , task_id: Int) {
            thread {
                kotlin.run {
                    val uri = cmdTodoVoiceTask_get(task_id)
                    var ret = httpControlFunc.instance.get(uri , token)
                    var msg = Message()
                    sendMessage(handler,ret,15,16)
                }
            }
        }

        fun todoVerityTask(handler: Handler , task_id: Int) {
            thread {
                kotlin.run {
                    val uri = cmdToduVerityTask_get(task_id)
                    var ret = httpControlFunc.instance.get(uri , token)
                    var msg = Message()
                    sendMessage(handler,ret,17,18)
                }
            }
        }

        fun getVerifyItems(handler: Handler , task_id: Int) {
            thread {
                kotlin.run {
                    val uri = cmdVerifyItems_get(task_id)
                    var ret = httpControlFunc.instance.get(uri , token)
                    var msg = Message()
                    sendMessage(handler,ret,19,-1)
                }
            }
        }

        fun verifyItem(handler: Handler , item_id: Int , result: String , review: String?) {
            thread {
                kotlin.run {
                    var uri = cmdVerifyItem_patch()
                    var para: JSONObject = JSONObject().put("item_id" , item_id)
                    para.put("result" , result)
                    para.put("review" , review)
                    var ret = httpControlFunc.instance.patch(uri , para , token)
                    sendMessage(handler,ret,21,22)
                }
            }
        }

        fun updateUserInfo(handler: Handler , sex: String , age: Int , place: String?) {
            thread {
                kotlin.run {
                    var uri = cmdUpdateUserInfo_patch()
                    var para: JSONObject = JSONObject().put("sex" , sex)
                    para.put("age" , age)
                    para.put("place" , place)
                    var ret = httpControlFunc.instance.patch(uri , para , token)
                    sendMessage(handler,ret,23,24)
                }
            }
        }

        fun getUserInfo(handler: Handler) {
            thread {
                kotlin.run {
                    val uri = cmdGetUserInfo_get()
                    var ret = httpControlFunc.instance.get(uri , token)
                    var msg = Message()
                    sendMessage(handler,ret,25,-1)
                }
            }
        }

        fun getMessages(handler: Handler) {
            thread {
                kotlin.run {
                    val uri = cmdMessage_get()
                    var ret = httpControlFunc.instance.get(uri , token)
                    var msg = Message()
                    sendMessage(handler,ret,27,-1)
                }
            }
        }

        ////////////////////end
        private val dialectMap = mapOf<String , Int>(Pair("普通话" , 10) , Pair("北京话" , 11) , Pair("上海话" , 12) ,
                Pair("天津话" , 13) , Pair("广东话" , 14) , Pair("四川话" , 15) , Pair("山东话" , 16) , Pair("东北话" , 17) ,
                Pair("河南话" , 18) , Pair("台湾话" , 19))

        private val banks:Array<String> = arrayOf("工商银行","建设银行","农业银行","中国银行","招商银行")

        fun getBankArray():Array<String>
        {
            return banks
        }
        fun getDialectArray(): Array<String> {
            return dialectMap.keys.toTypedArray()
        }

        fun getDialectID(dialect: String): Int? {
            return dialectMap[dialect]
        }

        fun getDialect(id: Int): String {
            for (dialect in dialectMap.keys) {
                if (dialectMap[dialect] == id)
                    return dialect
            }
            return ""
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