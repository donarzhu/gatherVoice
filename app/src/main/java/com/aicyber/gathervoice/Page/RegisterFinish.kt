package com.aicyber.gathervoice.Page

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register_finish.*
import android.widget.RadioButton



class RegisterFinish : AppCompatActivity() {
    private inner class RetultError
    {
        var error:String = ""
    }
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message?) {
            try {
                super.handleMessage(msg)
                when(msg!!.what)
                {
                    3->
                    {
                        global.updateUserInfo(this,sex,age,place)
                    }
                    4->
                    {
                        var retData = msg.data.get("info").toString()
                        val retMsg:RetultError? = Gson().fromJson(retData,RetultError::class.java)
                        if(retMsg!=null)
                            Toast.makeText(this@RegisterFinish,retMsg.error, Toast.LENGTH_SHORT).show()
                    }
                    23->
                    {
                        Toast.makeText(this@RegisterFinish,"注册成功，请返回登录！", Toast.LENGTH_SHORT).show()
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

    var sex = ""
    var age = 0
    var place =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_finish)
        val phone = intent.getStringExtra("user")
        val pwd = intent.getStringExtra("password")
        val vCode = intent.getStringExtra("code")

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val rb = findViewById<RadioButton>(checkedId)
            sex = rb.text.toString()
        }

        backRegButton.setOnClickListener{
            this.finish()
        }
        registerButton.setOnClickListener{
            if(sex.isNullOrEmpty())
            {
                Toast.makeText(this@RegisterFinish,"请选择性别!",Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            age = inputAge.text.toString().toInt()
            if(age<10||age>60)
            {
                Toast.makeText(this@RegisterFinish,"请输入正确的年龄!",Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            place = inputPlace.text.toString()
            if(place.isNullOrEmpty())
            {
                Toast.makeText(this@RegisterFinish,"请输入籍贯!",Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            global.register(handler,phone,pwd,vCode)
        }
    }
}
