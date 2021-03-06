package com.aicyber.gathervoice.page

import android.annotation.SuppressLint
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
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler(){
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
            try {
                global.verifyCode(handler, inputName.text.toString())
            }
            catch (e:Exception)
            {
                e.printStackTrace()
            }
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
            if(vCode.isNullOrEmpty())
            {
                Toast.makeText(this@RegisterPhone,"验证码错误", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(pwd != repwd)
            {
                Toast.makeText(this@RegisterPhone,"两次密码输入不一致", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //global.register(handler,phone,pwd,vCode)
            var intent = Intent(this,RegisterFinish::class.java)
            intent.putExtra("user",phone)
            intent.putExtra("password",pwd)
            intent.putExtra("code",vCode)
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this, nextButton, "regPage")
                            .toBundle())
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
