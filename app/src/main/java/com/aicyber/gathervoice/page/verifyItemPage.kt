package com.aicyber.gathervoice.page

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.AudioPlayFunc
import com.aicyber.gathervoice.control.global
import com.aicyber.gathervoice.data.VerifyItemInfo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_verify_item_page.*

class verifyItemPage : AppCompatActivity() {
    private inner class ResultData{
        var id=0
        var verify=0
        var todo_item=0
        var result=""
        var review:String?=null
        var finish_at=""
    }
    var itemInfo:VerifyItemInfo?=null
    var itemsTotal:Int = 0
    var pos:Int = 0
    var taskName = ""
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            try {
                when (msg!!.what) {
                    21->{
                        var retData = msg.data.get("info").toString()
                        val retMsg: ResultData? = Gson().fromJson(retData, ResultData::class.java)
                        val intent = Intent()
                        intent.putExtra("id",retMsg!!.id)
                        intent.putExtra("result",retMsg!!.result)
                        intent.putExtra("finish_at",retMsg.finish_at)
                        setResult(Activity.RESULT_OK,intent)
                        finish()
                    }
                }
            }
            catch (e:Exception)
            {
                e.printStackTrace()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_item_page)
        try {
            itemInfo = intent.getSerializableExtra("data") as VerifyItemInfo
            itemsTotal = intent.getIntExtra("count",0)
            pos = intent.getIntExtra("pos",0)
            taskName = intent.getStringExtra("taskName")

            voice_text.text = itemInfo!!.todo_item.task_item_text
            task_name_title.text = taskName
            task_name.text = taskName
            id_total.text = itemsTotal.toString()
            id_pos.text = (pos+1).toString()
            if(!itemInfo!!.todo_item.sound_len.isNullOrEmpty()) {
                verify_wav.text = itemInfo!!.todo_item.sound_len
                verify_wav.setOnClickListener{
                    try {
                        //Toast.makeText(this@verifyItemPage,"正在播放远请稍后...",Toast.LENGTH_SHORT).show()
                        AudioPlayFunc.instance.playAudio(this@verifyItemPage,itemInfo!!.todo_item.file_url)
                    }
                    catch (e:Exception)
                    {
                        e.printStackTrace()
                    }
                }
            }
        }
        catch (ex:Exception)
        {
            var message = ex.message
        }

        backButton.setOnClickListener{
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        ok_button.setOnClickListener {
            global.verifyItem(handler, itemInfo!!.id,"pass",null)
        }

        fail_button.setOnClickListener{
            if(edit_text.text.toString().length < 4)
            {
                Toast.makeText(this@verifyItemPage,"请输入错误文字！",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            global.verifyItem(handler, itemInfo!!.id,"fail",edit_text.text.toString())
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
