package com.aicyber.gathervoice.page

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.AdapterView
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import com.aicyber.gathervoice.data.ItemInfo
import com.aicyber.gathervoice.data.TaskInfo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_voice_task_list_page.*
import kotlinx.android.synthetic.main.task_item.view.*

class voiceTaskListPage : AppCompatActivity() {
    companion object {
        var my:voiceTaskListPage? = null
        var handler: Handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message?) {
                try {
                    when(msg!!.what)
                    {
                        11->{
                            var retData = msg.data.get("info").toString()
                            val retMsg: ItemInfo? = Gson().fromJson(retData, ItemInfo::class.java)
                            if(retMsg!=null)
                            {
                                if(my!!.currentView!=null)
                                {
                                    my!!.currentView!!.voice_len.text = retMsg.sound_len
                                    my!!.currentView!!.tag = retMsg
                                }
                            }
                        }
                        13->{
                            var retData = msg.data.get("info").toString()
                            if(retData.isNullOrEmpty())
                                return
                            val retMsg:Array<ItemInfo> = Gson().fromJson(retData, Array<ItemInfo>::class.java)
                            my!!.items.clear()
                            for(item:ItemInfo in retMsg)
                            {
                                my!!.items.add(item)
                            }
                            var adapter = TaskItemAdapter(my!!,my!!.items)
                            my!!.item_list.adapter = adapter
                            my!!.item_list.onItemClickListener = AdapterView.OnItemClickListener{
                                adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->run{
                                var info:ItemInfo = view1.tag as ItemInfo
                                my!!.currentView = view1
                                if(info==null)
                                    return@run
                                val bundle = Bundle()
                                bundle.putSerializable("data",info)
                                bundle.putInt("count",my!!.items.size)
                                bundle.putInt("pos",i)
                                bundle.putString("taskName",my!!.taskinfo!!.name)
                                my!!.intent = Intent(my!!,Record::class.java)
                                my!!.intent.putExtras(bundle)
                                global.sendFileHandler = this
                                my!!.startActivity(my!!.intent)
                            }}
                        }
                    }
                }
                catch (e:Exception)
                {
                    println(e.message)
                }
            }
        }

    }
    var taskinfo:TaskInfo? = null
    var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_task_list_page)
        my = this
        backButton.setOnClickListener {
            finish()
        }
        if(intent.extras==null)
            return
        taskinfo = intent.extras!!.getParcelable<TaskInfo>("data")
        id=intent.extras!!.getInt("id")
        if(taskinfo==null)
            return
        if(!taskinfo!!.name.isEmpty()) {
            task_name.text = taskinfo!!.name
            task_name_title.text = taskinfo!!.name
        }
        getItemList()
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
    private var items: ArrayList<ItemInfo> = ArrayList()
    private var currentView:View? = null
    private fun getItemList() {
        global.getVoiceTaskItems(handler,id)
    }
}
