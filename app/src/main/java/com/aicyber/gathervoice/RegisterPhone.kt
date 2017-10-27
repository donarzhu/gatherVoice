package com.aicyber.gathervoice

import android.app.ActivityOptions
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.util.AttributeSet
import kotlinx.android.synthetic.main.activity_register_phone.*
import java.time.Duration

class RegisterPhone : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_phone)
        //window.enterTransition = Explode().setDuration(2000)
        //window.exitTransition = Explode().setDuration(2000)
        backButton.setOnClickListener{
            startActivity(Intent(this, Login::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this, backButton, "loginPage")
                            .toBundle())

        }
    }
}
