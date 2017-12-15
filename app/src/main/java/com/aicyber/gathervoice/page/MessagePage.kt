package com.aicyber.gathervoice.page

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.os.Handler
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.aicyber.gathervoice.MessageService
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_message_page.*
import kotlinx.android.synthetic.main.message_card.view.*
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

class MessagePage : AppCompatActivity() {
    companion object {
        var my:MessagePage?=null
        private var messages:MutableList<MessageService.MessageData> = ArrayList()
    }
    var adapter:BaseAdapter =object: BaseAdapter() {
        override fun getItem(position: Int): Any {
            return MessagePage.messages[position]
        }

        override fun getItemId(position: Int): Long {
            return MessagePage.messages[position].id.toLong()
        }

        override fun getCount(): Int {
            return MessagePage.messages.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = LayoutInflater.from(this@MessagePage).inflate(R.layout.message_card,null)
            val info = messages[position]
            view.tag = info
            view.message_title.text = info.title
            view.body_text.text = info.body
            view.message_time.text = info.create_at
            view.delete_item.setOnClickListener{
                messages.remove(info)
                messageService!!.removeMessage(info)
                this.notifyDataSetChanged()
            }
            return view
        }
    }

    private var messageService:MessageService? = null
    private var serviceConnection = object:ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            messageService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            try {
                var binder:MessageService.MessageBinder = service as MessageService.MessageBinder
                messageService = binder.getService()
                if(messageService!=null)
                    getList(messageService!!.messages.toTypedArray())
            }
            catch (e:Exception)
            {
                e.printStackTrace()
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_page)
        try {
            my = this
            messages.clear()
            this.bindService(Intent(this,MessageService::class.java),serviceConnection,android.content.Context.BIND_AUTO_CREATE)
             backButton.setOnClickListener {
                finish()
            }
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
    }

    private fun getList(list:Array<MessageService.MessageData>)
    {
        messages = messages.plus(list).toMutableList()
        if(messages.size<=0)return
        message_list.adapter = adapter
        adapter.notifyDataSetChanged()
    }

}
