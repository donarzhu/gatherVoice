package com.aicyber.gathervoice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //this.titleText.setText("start app")
        this.setSupportActionBar(this.toolbar)
        this.tabLayout.addTab(this.tabLayout.newTab()?.setText("Tab 1"))
        this.tabLayout.addTab(this.tabLayout.newTab()?.setText("Tab 2"))
        this.tabLayout.addTab(this.tabLayout.newTab()?.setText("Tab 3"))
        this.emailButton.setOnClickListener{
            Toast.makeText(this,"OK",Toast.LENGTH_SHORT).show()
        }
    }
}
