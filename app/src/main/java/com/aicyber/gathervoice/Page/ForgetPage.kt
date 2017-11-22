package com.aicyber.gathervoice.Page

import android.app.ActivityOptions
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aicyber.gathervoice.R
import kotlinx.android.synthetic.main.activity_forget_page.*

class ForgetPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_page)
        backButton.setOnClickListener{
            this.finish()
        }

        goResetButton.setOnClickListener{
            startActivity(Intent(this, ResetPwdPage::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this, goResetButton, "forgetPage")
                            .toBundle())

        }
    }
}
