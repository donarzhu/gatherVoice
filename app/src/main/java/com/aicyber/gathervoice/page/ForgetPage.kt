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
import kotlinx.android.synthetic.main.activity_forget_page.*

class ForgetPage : AppCompatActivity() {
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
                        Toast.makeText(this@ForgetPage,retMsg.message, Toast.LENGTH_SHORT).show()
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_page)
        backButton.setOnClickListener{
            this.finish()
        }

        goResetButton.setOnClickListener{
            val vCode = getCode.text.toString()
            val phone = inputName.text.toString()
            if(vCode.length < 3)
            {
                Toast.makeText(this,"请输入正确的验证码！",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(phone.length != 11)
            {
                Toast.makeText(this,"请输入正确电话号码！",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var intent = Intent(this,ResetPwdPage::class.java)
            intent.putExtra("user",phone)
            intent.putExtra("code",vCode)
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this, goResetButton, "forgetPage")
                            .toBundle())

        }
        getCode_button.setOnClickListener{
            var error = false
            if(inputName.length() != 11) {
                inputNameWrapper.error = "手机号码输入错误"
                Toast.makeText(this@ForgetPage,"手机号码输入错误", Toast.LENGTH_SHORT).show()
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
    }
}
