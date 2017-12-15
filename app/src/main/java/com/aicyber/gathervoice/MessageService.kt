package com.aicyber.gathervoice

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock.sleep
import com.aicyber.gathervoice.control.global
import com.google.gson.Gson
import kotlin.concurrent.thread
import android.app.PendingIntent
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.aicyber.gathervoice.page.Login
import com.aicyber.gathervoice.page.MessagePage

@Suppress("DEPRECATION")
class MessageService : Service() {
    internal inner class MessageData
    {
        var id = 0
        var title = ""
        var body = ""
        var create_at = ""
    }

    internal inner class MessageBinder : Binder()
    {
        fun getService():MessageService {
            return this@MessageService
        }
    }
    private var binder = MessageBinder()


    private var pref: SharedPreferences?  = null
    private var editor: SharedPreferences.Editor? = null
    internal val messages:MutableList<MessageData> = ArrayList()
    companion object {
        internal fun showStandardNotification(context: Context,info:MessageData) {
            val notification = createNotificationBuider(context,
                    info.title, info.body)
            showNotification(context, notification.build(), 0)
        }
        internal fun showStandardNotification(context: Context,title: String,body:String) {
            val notification = createNotificationBuider(context,
                    title, body)
            showNotification(context, notification.build(), 0)
        }
        private fun createNotificationBuider(context: Context,
                                             title: String, message: String): NotificationCompat.Builder {
            val largeIcon = BitmapFactory.decodeResource(context.resources,
                    R.drawable.message_icon)
            val intent = Intent()
            intent.setClass(context,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            return NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.message_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setLargeIcon(largeIcon)
                    .setColor(ContextCompat.getColor(context, R.color.black))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
        }
        private fun showNotification(context: Context, notification: Notification, id: Int) {
            val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notification.defaults = Notification.DEFAULT_ALL
            mNotificationManager.notify(id, notification)
        }

    }
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: android.os.Message?) {
            try {
                var retData = msg!!.data.get("info").toString()
                val retMsg: Array<MessageData> = Gson().fromJson(retData, Array<MessageData>::class.java) ?: return
                for(info in retMsg)
                {
                    showStandardNotification(this@MessageService,info)
                }
                getList(retMsg)
                if(messages.size>0)
                    editor!!.putString("data", Gson().toJson(messages))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    internal fun removeMessage(data:MessageData)
    {
        for(info in messages)
        {
            if(data.id == info.id)
            {
                messages.remove(info)
                editor!!.putString("data", Gson().toJson(messages))
            }
        }
    }

    override fun onCreate() {
        //showStandardNotification(this,"服务启动","中信数据app启动")
        pref = getSharedPreferences(global.username,MODE_PRIVATE)
        editor = pref!!.edit()
        var datas = pref!!.getString("data","")
        if(!datas.isNullOrBlank())
        {
            var mlist = Gson().fromJson(datas, Array<MessageService.MessageData>::class.java)
            getList(mlist)
        }
        thread{
            kotlin.run{

                while (true)
                {
                    try {
                        global.getMessages(handler)
                        sleep(30000)
                    }
                    catch (e:Exception)
                    {
                        e.printStackTrace()
                    }
                }
            }
        }
        super.onCreate()
    }

    private fun getList(mlist: Array<MessageService.MessageData>?) {
        if (mlist != null) {
            messages += mlist
        }

    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    private var note: Notification? = null
    private fun initNotify(info:MessageData) {
        val builder = Notification.Builder(this)
                .setTicker(info.title)
                .setSmallIcon(R.drawable.message_icon)
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        note = builder.setContentIntent(pendingIntent)
                .setContentTitle(info.title)
                .setContentText(info.body)
                .build()
    }


}
