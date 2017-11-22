package com.aicyber.gathervoice.Page

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.data.TaskInfo
import com.aicyber.gathervoice.data.VerifyItemInfo
import com.aicyber.gathervoice.verifyItemPage
import kotlinx.android.synthetic.main.activity_check_task_page.*
class checkTaskPage : AppCompatActivity() {

    var taskinfo:TaskInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_task_page)
        backButton.setOnClickListener {
            finish()
        }
        if(intent.extras==null)
            return
        taskinfo= intent.extras!!.getParcelable<TaskInfo>("data")
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
        var adapter = VerifyItemAdapter(this,items)
        item_list.adapter = adapter
        item_list.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->run{
            var info:VerifyItemInfo = view1.tag as VerifyItemInfo
            if(info==null)
                return@run
            val bundle = Bundle()
            bundle.putSerializable("data",info)
            bundle.putInt("count",items.size)
            bundle.putInt("pos",i)
            bundle.putString("taskName",taskinfo!!.name)
            intent = Intent(this@checkTaskPage,verifyItemPage::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }}

    }

}
