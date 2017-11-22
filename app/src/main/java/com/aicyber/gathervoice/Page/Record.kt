package com.aicyber.gathervoice.Page

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.data.ItemInfo
import kotlinx.android.synthetic.main.activity_record.*
import com.aicyber.gathervoice.control.AudioRecordFunc
import com.aicyber.gathervoice.control.AudioRecordFunc.AudioFileFunc
import com.aicyber.gathervoice.control.AudioPlayFunc

class Record : AppCompatActivity() {
    var itemInfo:ItemInfo?=null
    var itemsTotal:Int = 0
    var pos:Int = 0
    var taskName = ""
    private var uiHandler: UIHandler? = null
    private var uiThread: UIThread? = null
    var isSuccedStop = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        itemInfo = intent.getSerializableExtra("data") as ItemInfo
        itemsTotal = intent.getIntExtra("count",0)
        pos = intent.getIntExtra("pos",0)
        taskName = intent.getStringExtra("taskName")
        uiHandler = UIHandler()

        task_name_title.text = taskName
        task_name.text = taskName
        id_total.text = itemsTotal.toString()
        id_pos.text = (pos+1).toString()
        record_button.setOnClickListener {  }
        record_button.setOnTouchListener { view, motionEvent ->
            //按下操作
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                try {
                    val mRecord = AudioRecordFunc.instance
                    val mResult = mRecord.startRecordAndFile()
                    isSuccedStop = false
                    if(mResult == AudioRecordFunc.ErrorCode.SUCCESS) {
                        //uiThread =  UIThread()
                        //Thread(uiThread).start()
                        Toast.makeText(this@Record,"正在录音中...",Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        val msg = Message()
                        val b = Bundle()// 存放数据
                        b.putInt("cmd", CMD_RECORDFAIL)
                        b.putInt("msg", mResult)
                        msg.data = b

                        uiHandler!!.sendMessage(msg) // 向Handler发送消息,更新UI
                    }
                }
                catch ( e:Exception)
                {
                    Toast.makeText(this@Record,e.message,Toast.LENGTH_SHORT).show()
                }

            }
            //抬起操作
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                try {
                    val mRecord_1 = AudioRecordFunc.instance
                    mRecord_1.stopRecordAndFile()
                    isSuccedStop = true
                    Toast.makeText(this@Record,"录音停止，保存文件中...",Toast.LENGTH_SHORT).show()

                    //if (uiThread != null) {
                    //    uiThread!!.stopThread()
                    //    uiThread!!.vRun = false
                    //}
                    if (uiHandler != null)
                        uiHandler!!.removeCallbacks(uiThread)
                    val msg = Message()
                    val b = Bundle()// 存放数据
                    b.putInt("cmd", CMD_STOP)
                    msg.data = b
                    uiHandler!!.sendMessageDelayed(msg, 1000) // 向Handler发送消息,更新UI
                }
                catch (e:Exception)
                {
                    Toast.makeText(this@Record,e.message,Toast.LENGTH_SHORT).show()
                }
            }
            //移动操作
            if (motionEvent.action == MotionEvent.ACTION_MOVE) {

            }
            false
        }

        final_record_button.setOnClickListener{
            finish()
        }

        re_record_button.setOnClickListener{
            layout_record.visibility = View.VISIBLE
            finish_layout.visibility = View.INVISIBLE

        }
        backButton.setOnClickListener{
            if(!isSuccedStop) {
                val mRecord_1 = AudioRecordFunc.instance
                mRecord_1.stopRecordAndFile()
                isSuccedStop = true
            }
            finish()
        }
    }


    private val CMD_RECORDING_TIME = 2000
    private val CMD_RECORDFAIL = 2001
    private val CMD_STOP = 2002

    internal inner class UIHandler : Handler()
    {
        override fun handleMessage(msg: Message)
        {
            // TODO Auto-generated method stub
            Log.d("MyHandler", "handleMessage......")
            super.handleMessage(msg)
            val b = msg.getData()
            val vCmd = b.getInt("cmd")
            when (vCmd)
            {
                CMD_RECORDING_TIME -> {
                    val vTime = b.getInt("msg")
                    Toast.makeText(this@Record,"正在录音中，已录制：$vTime s",Toast.LENGTH_SHORT).show()
                }
                CMD_RECORDFAIL -> {
                    val vErrorCode = b.getInt("msg")
                    val vMsg = AudioRecordFunc.ErrorCode.getErrorInfo(this@Record, vErrorCode)
                    Toast.makeText(this@Record,"录音失败：" + vMsg,Toast.LENGTH_SHORT).show()
                }
                CMD_STOP -> {
                    val mRecord_1 = AudioRecordFunc.instance
                    var mSize = mRecord_1.getRecordFileSize()
                    var timeString = AudioPlayFunc.instance.getRingDuring(this@Record,AudioFileFunc.wavFilePath)
                    voice_len.text = timeString
                    voice_len.setOnClickListener {
                        AudioPlayFunc.instance.playAudio(this@Record,AudioFileFunc.wavFilePath)
                    }
                    layout_record.visibility = View.INVISIBLE
                    finish_layout.visibility = View.VISIBLE
                    Toast.makeText(this@Record,"录音已停止.录音文件:" + AudioFileFunc.wavFilePath + "\n文件大小：" + mSize,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    internal inner class UIThread : Runnable {
        var mTimeMill = 0
        var vRun = true
        fun stopThread() {
            vRun = false
        }

        override fun run() {
            while (vRun) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

                mTimeMill++
                Log.d("thread", "mThread........" + mTimeMill)
                val msg = Message()
                val b = Bundle()// 存放数据
                b.putInt("cmd", CMD_RECORDING_TIME)
                b.putInt("msg", mTimeMill)
                msg.setData(b)

                this@Record.uiHandler!!.sendMessage(msg) // 向Handler发送消息,更新UI
            }

        }
    }

}

