package com.aicyber.gathervoice.page

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aicyber.gathervoice.R
import kotlinx.android.synthetic.main.activity_reset_pwd_page.*

class ResetPwdPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pwd_page)
        backButton.setOnClickListener {
            this.finish()
        }

    }
}
