package com.aicyber.gathervoice.Page

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import kotlinx.android.synthetic.main.activity_change_user_info_page.*

class ChangeUserInfoPage : AppCompatActivity() {
    var sex = ""
    var age = 0
    var place =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_info_page)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val rb = findViewById<RadioButton>(checkedId)
            sex = rb.text.toString()
        }

        backRegButton.setOnClickListener{
            this.finish()
        }

        updateButton.setOnClickListener{
            if(sex.isNullOrEmpty())
            {
                Toast.makeText(this@ChangeUserInfoPage,"请选择性别!", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            age = inputAge.text.toString().toInt()
            if(age<10||age>60)
            {
                Toast.makeText(this@ChangeUserInfoPage,"请输入正确的年龄!", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            place = inputPlace.text.toString()
            if(place.isNullOrEmpty())
            {
                Toast.makeText(this@ChangeUserInfoPage,"请输入籍贯!", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            global.updateUserInfo(global.sendDataHandler!!,sex,age,place)

            finish()
        }
    }
}
