package com.aicyber.gathervoice

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.aicyber.gathervoice.data.TaskInfo
import kotlinx.android.synthetic.main.my_task.view.*
import kotlinx.android.synthetic.main.task_card.*
import kotlinx.android.synthetic.main.task_card.view.*
import kotlinx.android.synthetic.main.my_task.*

/**
 * Created by donar on 2017/11/15.
 */
public class TaskViewAdapter(context:Context,list:ArrayList<TaskInfo>) : BaseAdapter()
{
    constructor(context:Context,list:ArrayList<TaskInfo>,vType:Int) : this(context,list) {
        viewType = vType
    }

    public final val taskCenterType:Int = 0
    public final val myTaskType:Int = 1
    public final val CheckTypeList = 1
    public final val VoiceTypeList = 0
    public val tasks: ArrayList<TaskInfo> =list
    private val ctx: Context = context

    public var listType = VoiceTypeList
    private var viewType:Int = taskCenterType

    override fun getCount(): Int {
        return tasks.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return tasks[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val taskInfo = tasks[position]
        when(viewType)
        {
            taskCenterType->{
                val view = LayoutInflater.from(ctx).inflate(R.layout.task_card,null)
                view.tag = taskInfo
                if(!taskInfo.name.isEmpty())
                    view.task_name.text = taskInfo.name
                if(!taskInfo.memo.isEmpty())
                    view.task_desc.text = taskInfo.memo
                if(!taskInfo.status.isEmpty())
                    view.task_status.text = taskInfo.status
                when(listType ){
                    VoiceTypeList->if(!taskInfo.reward.isEmpty()) view.task_price.text = taskInfo.reward
                    CheckTypeList->if(!taskInfo.v_reward.isEmpty()) view.task_price.text = taskInfo.v_reward
                }
                return view
            }
            myTaskType->{
                val view = LayoutInflater.from(ctx).inflate(R.layout.my_task,null)
                view.tag = taskInfo
                if(!taskInfo.name.isEmpty())
                    view.my_task_name.text = taskInfo.name
                if(!taskInfo.memo.isEmpty())
                    view.my_task_desc.text = taskInfo.memo
                when(listType){
                    VoiceTypeList->if(!taskInfo.finish_at.isEmpty())view.my_task_status.text = "截止日期："+taskInfo.finish_at.subSequence(0,9)
                    CheckTypeList->if(!taskInfo.v_finish_at.isEmpty())view.my_task_status.text = "截止日期："+taskInfo.v_finish_at.subSequence(0,9)
                }
                when(listType ){
                    VoiceTypeList-> {
                        if (!taskInfo.reward.isEmpty()) view.my_task_price.text = taskInfo.reward
                        view.type_image.setImageResource(R.drawable.voice)
                    }
                    CheckTypeList-> {
                        if (!taskInfo.v_reward.isEmpty()) view.my_task_price.text = taskInfo.v_reward
                        view.type_image.setImageResource(R.drawable.check)
                    }
                }
                return view
            }
        }
        throw (Exception("no type!"))
    }

}