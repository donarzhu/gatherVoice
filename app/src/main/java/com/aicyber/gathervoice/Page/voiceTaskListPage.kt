package com.aicyber.gathervoice.Page

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.BaseAdapter
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.data.ItemInfo
import com.aicyber.gathervoice.data.TaskInfo
import kotlinx.android.synthetic.main.activity_voice_task_list_page.*

class voiceTaskListPage : AppCompatActivity() {

    var taskinfo:TaskInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_task_list_page)
        backButton.setOnClickListener {
            finish()
        }
        if(intent.extras==null)
            return
        taskinfo = intent.extras!!.getParcelable<TaskInfo>("data")
        if(taskinfo==null)
            return
        if(!taskinfo!!.name.isEmpty()) {
            task_name.text = taskinfo!!.name
            task_name_title.text = taskinfo!!.name
        }
        getItemList()
    }
    private var items: ArrayList<ItemInfo> = ArrayList()
    private fun getItemList() {
        var i=0;
        while(i<10)
        {
            var info:ItemInfo = ItemInfo()
            info.id=i
            info.task_item=i
            items.add(info)
            i++
        }
        var adapter = TaskItemAdapter(this,items)
        item_list.adapter = adapter
        item_list.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->run{
            var info:ItemInfo = view1.tag as ItemInfo
            if(info==null)
                return@run
            val bundle = Bundle()
            bundle.putSerializable("data",info)
            bundle.putInt("count",items.size)
            bundle.putInt("pos",i)
            bundle.putString("taskName",taskinfo!!.name)
            intent = Intent(this,Record::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }}

    }
}
