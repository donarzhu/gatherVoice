package com.aicyber.gathervoice.page

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_reset_pwd_page.*

class ResetPwdPage : AppCompatActivity() {
    private inner class RetultMessge
    {
        var message:String = ""
    }
    private var vcode = "000000"
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message?) {
            try {
                super.handleMessage(msg)
                when(msg!!.what)
                {
                    1->{
                        getCode.text.clear()
                        getCode.text.append("000000")
                    }
                    2,31,32->{
                        var retData = msg.data.get("info").toString()
                        val retMsg:RetultMessge? = Gson().fromJson(retData,RetultMessge::class.java)
                        if(retMsg!=null)
                            Toast.makeText(this@ResetPwdPage,retMsg.message, Toast.LENGTH_SHORT).show()

                    }
                }

            }
            catch (e:Exception)
            {
                var msg = e.message
                println(msg)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pwd_page)
        backButton.setOnClickListener {
            this.finish()
        }

        getVerityCode.setOnClickListener {
            var error = false
            if (inputName.length() != 11) {
                inputNameWrapper.error = "手机号码输入错误"
                Toast.makeText(this@ResetPwdPage, "手机号码输入错误", Toast.LENGTH_SHORT).show()
                error = true
            }
            if (error)
                return@setOnClickListener
            try {
                global.verifyCode(handler, inputName.text.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        finalButton.setOnClickListener{
            var pwd = inputPwd.text.toString()
            var repwd = reinputPwd.text.toString()
            var error = false
            if(pwd.length <= 6)
            {
                Toast.makeText(this@ResetPwdPage,"请输入6为以上的密码",Toast.LENGTH_LONG).show()
                error = true
            }
            if(pwd != repwd)
            {
                Toast.makeText(this@ResetPwdPage,"两次密码输入不一致",Toast.LENGTH_LONG).show()
                error = true
            }
            if(getCode.text.toString() != vcode)
            {
                global.resetPwd(handler,pwd,vcode)
            }

        }
    }
}
