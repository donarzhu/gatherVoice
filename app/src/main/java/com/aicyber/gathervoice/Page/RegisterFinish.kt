package com.aicyber.gathervoice.Page

import android.app.ActivityOptions
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aicyber.gathervoice.R
import kotlinx.android.synthetic.main.activity_register_finish.*
import kotlinx.android.synthetic.main.activity_register_phone.*

class RegisterFinish : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_finish)
        backRegButton.setOnClickListener{
            this.finish()
        }
    }
}
