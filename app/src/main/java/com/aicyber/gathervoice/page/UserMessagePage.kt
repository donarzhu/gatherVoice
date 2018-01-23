package com.aicyber.gathervoice.page

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import kotlinx.android.synthetic.main.activity_user_message_page.*

class UserMessagePage : AppCompatActivity() {
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            when(msg?.what)
            {
                39->finish()
                40->Toast.makeText(this@UserMessagePage,"提交失败",Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_message_page)
        submit_user.setOnClickListener{
            var message = Edit_Message.text.toString()
            if(message.length<4)
            {
                Toast.makeText(this,"内容太短，请写详细写！",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            global.userMessage(handler,message)
        }
        backButton.setOnClickListener {
            finish()
        }
    }
}
