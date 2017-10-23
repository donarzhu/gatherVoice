package com.aicyber.gathervoice

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class Login : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}

