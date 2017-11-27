package com.aicyber.gathervoice.Page

import android.app.ActivityOptions
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register_phone.*

class RegisterPhone : AppCompatActivity() {
    private inner class RetultMessge
    {
        var message:String = ""
    }
    private inner class RetultError
    {
        var error:String = ""
    }
    var handler: Handler = object : Handler(){
        override fun handleMessage(msg: Message?) {
            try {
                super.handleMessage(msg)
                if(msg!!.what <= 2)
                {
                    var retData = msg.data.get("info").toString()
                    val retMsg:RetultMessge? = Gson().fromJson(retData,RetultMessge::class.java)
                    if(retMsg!=null)
                        Toast.makeText(this@RegisterPhone,retMsg.message, Toast.LENGTH_SHORT).show()
                }
                when(msg!!.what)
                {
                    1->{
                        getCode.text.clear()
                        getCode.text.append("000000")
                    }
                    3->
                    {
                        Toast.makeText(this@RegisterPhone,"注册成功，请返回登录！", Toast.LENGTH_SHORT).show()
                    }
                    4->
                    {
                        var retData = msg.data.get("info").toString()
                        val retMsg:RetultError? = Gson().fromJson(retData,RetultError::class.java)
                        if(retMsg!=null)
                            Toast.makeText(this@RegisterPhone,retMsg.error, Toast.LENGTH_SHORT).show()
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
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_register_phone)
        }
        catch (e:Exception)
        {
            println(e.message)
            println(e.stackTrace)
            return
        }
        //window.enterTransition = Explode().setDuration(2000)
        //window.exitTransition = Explode().setDuration(2000)

        backButton.setOnClickListener{
            this.finish()
        }

        getVerityCode.setOnClickListener{
            var error = false
            if(inputName.length() != 11) {
                inputNameWrapper.error = "手机号码输入错误"
                Toast.makeText(this@RegisterPhone,"手机号码输入错误", Toast.LENGTH_SHORT).show()
                error = true
            }
            if(error)
                return@setOnClickListener
            global.verifyCode(handler,inputName.text.toString())
        }
        nextButton.setOnClickListener{
            if(inputName.length() != 11) {
                inputNameWrapper.error = "手机号码输入错误"
                Toast.makeText(this@RegisterPhone,"手机号码输入错误", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val vCode = getCode.text.toString()
            val phone = inputName.text.toString()
            val pwd = inputPwd.text.toString()
            val repwd = reinputPwd.text.toString()
            if(vCode != "000000")
            {
                Toast.makeText(this@RegisterPhone,"验证码错误", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(pwd != repwd)
            {
                Toast.makeText(this@RegisterPhone,"两次密码输入不一致", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            global.register(handler,phone,pwd,vCode)
            /*
            startActivity(Intent(this,RegisterFinish::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this, nextButton, "regPage")
                            .toBundle())*/
        }
    }
}
