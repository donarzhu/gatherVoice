package com.aicyber.gathervoice.page

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import kotlinx.android.synthetic.main.activity_change_pwd_page.*

class ChangePwdPage : AppCompatActivity() {
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            when(msg?.what)
            {
                33->Toast.makeText(this@ChangePwdPage,"修改密码成功",Toast.LENGTH_LONG).show()
                34->Toast.makeText(this@ChangePwdPage,"修改失败，请检查后再试！",Toast.LENGTH_LONG).show()
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pwd_page)
        finalButton.setOnClickListener {
            var oldPwd = inputOldPwd.text.toString()
            var pwd = inputPwd.text.toString()
            var rePwd = reinputPwd.text.toString()
            if(oldPwd.length<6 || pwd.length<6 || pwd!=rePwd)
            {
                Toast.makeText(this,"输入错误，请检查后再输",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            global.changePwd(handler,oldPwd,pwd)
        }
        backButton.setOnClickListener {
            finish()
        }
    }
}
