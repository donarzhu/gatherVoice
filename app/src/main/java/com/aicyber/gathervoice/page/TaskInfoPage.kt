package com.aicyber.gathervoice.page

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import com.aicyber.gathervoice.data.TaskInfo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_task_info_page.*

class TaskInfoPage : AppCompatActivity() {
    private inner class todoTaskResult{
        var code:Int=0 //1005,
        var error:String="" //"录音任务您已认领过"
    }
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            try {
                when (msg!!.what) {
                    15,17->finish()
                    16,18->{
                        var retData = msg.data.get("info").toString()
                        val retMsg: todoTaskResult? = Gson().fromJson(retData, todoTaskResult::class.java)
                        if(retMsg!=null)
                        {
                            Toast.makeText(this@TaskInfoPage,retMsg.error,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
    private var type:Int = 0
    private var verify_id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_info_page)
        try {
            //val taskinfo = this.intent.getParcelableExtra<TaskInfo>("data")
            var datas: Bundle? = intent.extras
            val taskinfo = datas!!.getParcelable<TaskInfo>("data")
            type = datas!!.getInt("type",0)
            if(type==1)
            {
                verify_id = datas!!.getInt("verify_id",0)
            }
            if (taskinfo != null) {
                if (!taskinfo!!.name.isEmpty()) task_name_page.text = taskinfo!!.name
                if (!taskinfo!!.memo.isEmpty()) text_desc.text = taskinfo!!.memo
                when(type)
                {
                    0->if (!taskinfo!!.reward.isEmpty()) text_price.text = (taskinfo!!.reward.toFloat()*taskinfo.item_count).toString()
                    1->if(!taskinfo!!.v_reward.isNullOrEmpty()) text_price.text = (taskinfo!!.v_reward.toFloat()*taskinfo.item_count).toString()
                }
            }
            addTaskButton.setOnClickListener{
                //setResult(Activity.RESULT_OK)
                when(type)
                {
                    0->{
                        global.todoVoiceTask(handler,taskinfo.id)
                    }
                    1->{
                        global.todoVerityTask(handler,verify_id)
                    }
                }
            }
        }
        catch (e:Exception)
        {
            var mssage = e.message
        }

        backMainButton.setOnClickListener{
            finish()
        }

    }
}
