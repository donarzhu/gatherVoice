package com.aicyber.gathervoice.page

/**
 * Created by donar on 2017/11/20.
 */
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.data.ItemInfo
import com.aicyber.gathervoice.data.VerifyItemInfo
import kotlinx.android.synthetic.main.task_item.view.*

class TaskItemAdapter(context: Context, list:ArrayList<ItemInfo>) : BaseAdapter()
{
    var mContext = context
    var mList = list
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.task_item,null)
        var itemInfo = mList[position]
        view.tag = itemInfo
        if(!itemInfo.task_item_text.isNullOrEmpty())
            view.item_context.text = itemInfo.task_item_text
        if(!itemInfo.sound_len.isNullOrEmpty())
            view.voice_len.text = itemInfo.sound_len
        if(itemInfo.decision != null)
        {
            if(itemInfo.decision!!)
                view.check_status.text = "通过"
            else
                view.check_status.text = "未通过"
        }
        else
            view.check_status.text = "未审核"
        return view
    }

    override fun getItem(position: Int): Any {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mList.size
    }

}

class VerifyItemAdapter(context: Context, list:ArrayList<VerifyItemInfo>) : BaseAdapter()
{
    var mContext = context
    var mList = list
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.task_item,null)
        var itemInfo = mList[position]
        view.tag = itemInfo
        if(!itemInfo.todo_item.task_item_text.isNullOrEmpty())
            view.item_context.text = itemInfo.todo_item.task_item_text
        if(!itemInfo.todo_item.sound_len.isNullOrEmpty())
            view.voice_len.text = itemInfo.todo_item.sound_len
        if(!itemInfo.result.isNullOrEmpty()) {
            when(itemInfo.result)
            {
                "pass"-> view.check_status.text ="通过"
                "fail"-> view.check_status.text ="错误"
            }
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mList.size
    }

}
