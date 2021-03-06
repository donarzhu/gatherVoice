package com.aicyber.gathervoice.data

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by donar on 2017/11/15.
 */
public class TaskInfo() : Parcelable {
    public var id:Int = 0 // task_id
    public var name:String ="" // "task2",
    public var memo:String="" // "task2_memo",
    public var person:Int = 0
    public var male:Int =0
    public var female:Int =0
    public var age_from:Int = 0
    public var age_to:Int = 0
    public var place:String?=null
    public var item_count:Int= 0
    public var reward:String=""
    public var status:String=""
    public var rank:Int=0
    public var v_person:Int=0
    public var v_reward:String=""
    public var dialect:Int = 0
    public var format_dialect=""
    public var format_process =""
    public var todo_total:Int=0        // 领取录音任务人数
    public var todo_finish:Int=0       // 录音任务完成人数
    public var verify_total:Int=0      // 领取录音任务的验证任务人数
    public var verify_finish:Int=0     // 领取录音任务的验证任务完成人数
    public var create_at:String=""
    public var finish_at:String=""     //录音任务截止时间，小于可以领录音任务，大于可以领验证任务
    public var v_finish_at:String=""    //验证任务

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        memo = parcel.readString()
        person = parcel.readInt()
        male = parcel.readInt()
        female = parcel.readInt()
        age_from = parcel.readInt()
        age_to = parcel.readInt()
        place = parcel.readString()
        item_count = parcel.readInt()
        reward = parcel.readString()
        status = parcel.readString()
        rank = parcel.readInt()
        v_person = parcel.readInt()
        v_reward = parcel.readString()
        dialect = parcel.readInt()
        format_dialect = parcel.readString()
        format_process = parcel.readString()
        todo_total = parcel.readInt()
        todo_finish = parcel.readInt()
        verify_total = parcel.readInt()
        verify_finish = parcel.readInt()
        create_at = parcel.readString()
        finish_at = parcel.readString()
        v_finish_at = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(memo)
        parcel.writeInt(person)
        parcel.writeInt(male)
        parcel.writeInt(female)
        parcel.writeInt(age_from)
        parcel.writeInt(age_to)
        parcel.writeString(place)
        parcel.writeInt(item_count)
        parcel.writeString(reward)
        parcel.writeString(status)
        parcel.writeInt(rank)
        parcel.writeInt(v_person)
        parcel.writeString(v_reward)
        parcel.writeInt(dialect)
        parcel.writeString(format_dialect)
        parcel.writeString(format_process)
        parcel.writeInt(todo_total)
        parcel.writeInt(todo_finish)
        parcel.writeInt(verify_total)
        parcel.writeInt(verify_finish)
        parcel.writeString(create_at)
        parcel.writeString(finish_at)
        parcel.writeString(v_finish_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskInfo> {
        override fun createFromParcel(parcel: Parcel): TaskInfo {
            return TaskInfo(parcel)
        }

        override fun newArray(size: Int): Array<TaskInfo?> {
            return arrayOfNulls(size)
        }
    }
}

public class VerifyTask
{
    var id:Int = 0
    var receiver:Int = 0
    var task:TaskInfo?=null
    var total:Int = 0                 // 录音条目总数
    var finish:Int = 0                // 录音条目完成数
    var verify_total:Int = 0          // 认领验证任务总人数
    var verify_finish =  0         // 认领验证任已完成人数
    var accept_at:String= ""      // 录音任务认领时间
    var finish_at:String = ""       // 录音任务完成时间
}

public class MyTaskInfo
{
    var id:Int =0
    var receiver:Int =0
    var task:TaskInfo?=null
    var total:Int =0         //总项目数
    var finish:Int=0        //其中已录完项目数
    var accept_at:String = "" // "2017-10-11T00:57:20.196756",  //认领录音任务时间
    var finish_at:String? = null   //全部录完时间, null为未全部录完

}

public class MyVerityTask
{
    var id:Int = 0        // verify_id
    var verifier:Int =0   // 验证人id，是当前用户id
    var todo:VerifyTask?=null
    var total:Int =0             // 验证任务条目总数
    var finish:Int = 0            // 验证任务条目已完成数
    var accept_at:String=""  // "2017-10-15T13:56:38.326007",  // 接收验证任务时间
    var finish_at:String?= null       // 完成验证任务时间

}