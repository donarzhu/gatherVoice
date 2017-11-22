package com.aicyber.gathervoice.Page

import android.app.ActivityOptions
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aicyber.gathervoice.R
import kotlinx.android.synthetic.main.activity_register_phone.*

class RegisterPhone : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_phone)
        //window.enterTransition = Explode().setDuration(2000)
        //window.exitTransition = Explode().setDuration(2000)
        backButton.setOnClickListener{
            this.finish()
        }
        nextButton.setOnClickListener{
            startActivity(Intent(this,RegisterFinish::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this, nextButton, "regPage")
                            .toBundle())

        }
    }
}
