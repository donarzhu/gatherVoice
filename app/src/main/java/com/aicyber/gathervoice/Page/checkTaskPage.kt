package com.aicyber.gathervoice.Page

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.AdapterView
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import com.aicyber.gathervoice.data.TaskInfo
import com.aicyber.gathervoice.data.VerifyItemInfo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_check_task_page.*
import kotlinx.android.synthetic.main.task_item.view.*

class checkTaskPage : AppCompatActivity() {

    companion object {
        val VerifyRequestCode = 101
        private var currentView:View?=null
        private var my:checkTaskPage? = null
        private var handler: Handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message?) {
                when(msg!!.what)
                {
                    19->{
                        var retData = msg.data.get("info").toString()
                        if(retData.isNullOrEmpty())
                            return
                        val retMsg:Array<VerifyItemInfo> = Gson().fromJson(retData, Array<VerifyItemInfo>::class.java)
                        my!!.items.clear()
                        for(item:VerifyItemInfo in retMsg)
                        {
                            my!!.items.add(item)
                        }
                        var adapter = VerifyItemAdapter(my!!,my!!.items)
                        my!!.item_list.adapter = adapter
                        my!!.item_list.onItemClickListener = AdapterView.OnItemClickListener{
                            adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->run{
                            var info:VerifyItemInfo = view1.tag as VerifyItemInfo
                            if(info==null)
                                return@run
                            currentView = view1
                            val bundle = Bundle()
                            bundle.putSerializable("data",info)
                            bundle.putInt("count",my!!.items.size)
                            bundle.putInt("pos",i)
                            bundle.putString("taskName",my!!.taskinfo!!.name)
                            my!!.intent = Intent(my, verifyItemPage::class.java)
                            my!!.intent.putExtras(bundle)
                            try {
                                my!!.startActivityForResult(my!!.intent,VerifyRequestCode)
                            }
                            catch (e:Exception)
                            {
                                e.printStackTrace()
                            }
                        }}
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode != VerifyRequestCode)
            return
        if(resultCode != Activity.RESULT_OK)
            return
        if(data== null)
            return
        var id = data.getIntExtra("id",0)
        var result = data.getStringExtra("result")
        var fDate = data.getStringExtra("finish_at")
        when(result)
        {
            "pass"-> currentView!!.check_status.text ="通过"
            "fail"-> currentView!!.check_status.text ="错误"
        }

    }

    private var taskinfo:TaskInfo? = null
    private var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_task_page)
        my = this
        backButton.setOnClickListener {
            finish()
        }
        if(intent.extras==null)
            return
        taskinfo= intent.extras!!.getParcelable<TaskInfo>("data")
        id= intent.extras.getInt("id")
        if(taskinfo==null)
            return
        if(!taskinfo!!.name.isEmpty()) {
            task_name.text = taskinfo!!.name
            task_name_title.text = taskinfo!!.name
        }
        getItemList()
    }

    private var items: ArrayList<VerifyItemInfo> = ArrayList()
    private fun getItemList() {
        global.getVerifyItems(handler,id)
        /*
        var i=0;
        while(i<10)
        {
            var info = VerifyItemInfo()
            info.id=i
            info.todo_item.id=i
            info.todo_item.task_item=i
            items.add(info)
            i++
        }
        */


    }

}
