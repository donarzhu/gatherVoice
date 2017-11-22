package com.aicyber.gathervoice.Page

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aicyber.gathervoice.MainActivity
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.data.TaskInfo
import kotlinx.android.synthetic.main.activity_task_info_page.*

class TaskInfoPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_info_page)
        try {
            //val taskinfo = this.intent.getParcelableExtra<TaskInfo>("data")
            var datas: Bundle? = intent.extras
            val taskinfo = datas!!.getParcelable<TaskInfo>("data")

            if (taskinfo != null) {
                if (!taskinfo!!.name.isEmpty()) task_name_page.text = taskinfo!!.name
                if (!taskinfo!!.memo.isEmpty()) text_desc.text = taskinfo!!.memo
                if (!taskinfo!!.reward.isEmpty()) text_price.text = taskinfo!!.reward
            }
        }
        catch (e:Exception)
        {
            var mssage = e.message
        }

        backMainButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //setResult(Activity.RESULT_CANCELED)
            finish()
        }

        addTaskButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            //setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
