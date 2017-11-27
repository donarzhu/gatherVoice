package com.aicyber.gathervoice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.aicyber.gathervoice.control.global
import com.aicyber.gathervoice.data.VerifyItemInfo
import kotlinx.android.synthetic.main.activity_verify_item_page.*

class verifyItemPage : AppCompatActivity() {
    var itemInfo:VerifyItemInfo?=null
    var itemsTotal:Int = 0
    var pos:Int = 0
    var taskName = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_item_page)
        try {
            itemInfo = intent.getSerializableExtra("data") as VerifyItemInfo
            itemsTotal = intent.getIntExtra("count",0)
            pos = intent.getIntExtra("pos",0)
            taskName = intent.getStringExtra("taskName")

            task_name_title.text = taskName
            task_name.text = taskName
            id_total.text = itemsTotal.toString()
            id_pos.text = (pos+1).toString()
        }
        catch (ex:Exception)
        {
            var message = ex.message
        }

        backButton.setOnClickListener{
            finish()
        }

        ok_button.setOnClickListener {

        }

        fail_button.setOnClickListener{

        }
    }
}
