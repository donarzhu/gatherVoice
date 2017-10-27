package com.aicyber.gathervoice

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class Login : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.exitTransition = Explode()
        setContentView(R.layout.activity_login)
        buttonRegister.paint.flags= Paint.UNDERLINE_TEXT_FLAG
        buttonForget.paint.flags=Paint.UNDERLINE_TEXT_FLAG
        loginButton.setOnClickListener{
            var phoneNo = inputName.text.toString()
            var error = false;
            if(phoneNo.length != 11) {
                inputNameWrapper.error = "手机号码输入错误";
                error = true
            }
            var pwd = inputPwd.text.toString()
            if(pwd.length <6) {
                inputPwd.error = "密码错误"
                error = true
            }
            if(error)
                return@setOnClickListener

        }

        buttonRegister.setOnClickListener{
            val intent = Intent(this,RegisterPhone::class.java)
            val intents =Array<Intent>(1,{intent})
            val options = ActivityOptions.makeSceneTransitionAnimation(this)//ActivityOptions.makeSceneTransitionAnimation(this,loginView,"loginPage")
            startActivities(intents,options.toBundle())
        }
    }
}

