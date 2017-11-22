package com.aicyber.gathervoice.data

import android.os.Parcelable
import java.io.Serializable

/**
 * Created by donar on 2017/11/20.
 */
public class ItemInfo: Serializable {
    public var id:Int=0
    public var task_item:Int=0
    public var task_item_text:String=""
    public var file_url:String?=null
    public var sound_len:String?=null
    public var finish_at:String?=null
}

