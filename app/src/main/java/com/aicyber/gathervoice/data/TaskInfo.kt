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
    public var place:String?=null
    public var item_count:Int= 0
    public var reward:String=""
    public var status:String=""
    public var rank:Int=0
    public var v_person:Int=0
    public var v_reward:String=""
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
        place = parcel.readString()
        item_count = parcel.readInt()
        reward = parcel.readString()
        status = parcel.readString()
        rank = parcel.readInt()
        v_person = parcel.readInt()
        v_reward = parcel.readString()
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
        parcel.writeString(place)
        parcel.writeInt(item_count)
        parcel.writeString(reward)
        parcel.writeString(status)
        parcel.writeInt(rank)
        parcel.writeInt(v_person)
        parcel.writeString(v_reward)
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