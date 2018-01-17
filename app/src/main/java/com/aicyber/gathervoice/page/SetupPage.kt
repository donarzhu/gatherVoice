package com.aicyber.gathervoice.page

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aicyber.gathervoice.R
import kotlinx.android.synthetic.main.activity_setup_page.*

class SetupPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_page)
        chang_pwd.setOnClickListener {
            var intent = Intent(this,ChangePwdPage::class.java)
            startActivity(intent)
        }
        help.setOnClickListener{
            var intent = Intent(this,UserMessagePage::class.java)
            startActivity(intent)
        }
        backButton.setOnClickListener{
            finish()
        }

    }
}
