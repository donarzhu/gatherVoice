package com.aicyber.gathervoice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val service = Intent(context,MessageService::class.java)
        context.startService(service)
    }
}
