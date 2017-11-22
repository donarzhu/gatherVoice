package com.aicyber.gathervoice.data

import java.io.Serializable

/**
 * Created by donar on 2017/11/20.
 */
public class VerifyItemInfo: Serializable {
    public var id:Int=0
    public var verify:Int = 0
    public var todo_item:ItemInfo=ItemInfo()
    public var result:String?=null
    public var review:String?=null
    public var finish_at:String?=null
}