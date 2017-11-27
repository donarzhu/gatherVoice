package com.aicyber.gathervoice.Page

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.media.AudioFormat
import android.os.Bundle
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import com.aicyber.gathervoice.R
import kotlinx.android.synthetic.main.activity_login.*
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Toast
import android.content.DialogInterface
import android.os.Handler
import android.os.Message
import com.aicyber.gathervoice.MainActivity
import com.aicyber.gathervoice.control.global


class Login : AppCompatActivity() {

    var bCanRecord = false
    var bCanWrite = false
    var bNet = false
    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            try {
                when(msg!!.what)
                {
                    5->{
                        startActivity(Intent(this@Login, MainActivity::class.java),ActivityOptions.makeSceneTransitionAnimation(this@Login).toBundle())
                        finish()
                    }
                    6->
                    {
                        Toast.makeText(this@Login,"用户名或密码错误",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            catch (e:Exception)
            {
                println(e.message)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {

            //window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            //window.exitTransition = Explode()
            setContentView(R.layout.activity_login)
            buttonRegister.paint.flags= Paint.UNDERLINE_TEXT_FLAG
            buttonForget.paint.flags=Paint.UNDERLINE_TEXT_FLAG
            if(checkSelfPermission(RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)
            {
                val perms = arrayOf("android.permission.RECORD_AUDIO")
                val permsRequestCode = 200
                requestPermissions(perms, permsRequestCode)
            }
            else
            {
                bCanRecord = true
                if(checkSelfPermission(WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
                {
                    val perms = arrayOf("android.permission.WRITE_EXTERNAL_STORAGE")
                    val permsRequestCode = 300
                    requestPermissions(perms, permsRequestCode)
                }
                else
                {
                    bCanWrite = true
                    if(checkSelfPermission(INTERNET)!=PackageManager.PERMISSION_GRANTED)
                    {
                        val perms = arrayOf("android.permission.INTERNET")
                        val permsRequestCode = 400
                        requestPermissions(perms, permsRequestCode)
                    }
                    else
                    {
                        bNet = true
                    }
                }
            }

        }
        catch (e:Exception)
        {
            //Toast.makeText(this@Login,e.message,Toast.LENGTH_SHORT).show()
        }
        loginButton.setOnClickListener{
            if(!bCanRecord || !bCanWrite || !bNet)
            {
                AlertDialog.Builder(this@Login).setTitle("系统提示")//设置对话框标题

                        .setMessage("请同意必要权限，否则无法继续程序，将退出程序！")//设置显示的内容

                        .setPositiveButton("确定", DialogInterface.OnClickListener { dialog, which ->

                            finish()
                        }).show()//在按键响应事件中显示此对话框
                return@setOnClickListener
            }
            var phoneNo = inputName.text.toString()
            var error = false;
            if(phoneNo.length != 11) {
                inputNameWrapper.error = "手机号码输入错误";
                error = true
            }
            var pwd = inputPwd.text.toString()
            if(pwd.length <6) {
                inputPwd.error = "密码错误"
                error = true
            }
            if(error)
                return@setOnClickListener
            global.login(handler,phoneNo,pwd)
        }

        buttonRegister.setOnClickListener{
            //val intent = Intent(this,RegisterPhone::class.java)
            //val intents =Array<Intent>(1,{intent})
            //startActivity(Intent(this,RegisterPhone::class.java),ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            startActivity(Intent(this, RegisterPhone::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this, loginView, "loginPage")
                            .toBundle())
        }

        buttonForget.setOnClickListener{
            startActivity(Intent(this,ForgetPage::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this, loginView, "loginPage")
                            .toBundle())
        }
    }

    @SuppressLint("NewApi")
    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (permsRequestCode) {
            200 -> {
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if(!cameraAccepted)
                    Toast.makeText(this@Login,"没有录音权限",Toast.LENGTH_SHORT).show()
                bCanRecord = cameraAccepted
                if(checkSelfPermission(WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
                {
                    val perms = arrayOf("android.permission.WRITE_EXTERNAL_STORAGE")
                    val permsRequestCode = 300
                    requestPermissions(perms, permsRequestCode)
                }
                else
                {
                    bCanWrite = true
                }
            }
            300->{
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if(!cameraAccepted)
                    Toast.makeText(this@Login,"没有写权限",Toast.LENGTH_SHORT).show()
                bCanWrite = cameraAccepted
                if(checkSelfPermission(INTERNET)!=PackageManager.PERMISSION_GRANTED)
                {
                    val perms = arrayOf("android.permission.INTERNET")
                    val permsRequestCode = 400
                    requestPermissions(perms, permsRequestCode)
                }
                else
                {
                    bNet = true
                }

            }
            400->{
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if(!cameraAccepted)
                    Toast.makeText(this@Login,"没有访问存储设备权限",Toast.LENGTH_SHORT).show()
                bNet = cameraAccepted
            }
        }
    }

    object CheckAudioPermission {
        // 音频获取源
        var audioSource = MediaRecorder.AudioSource.MIC
        // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
        var sampleRateInHz = 44100
        // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
        var channelConfig = AudioFormat.CHANNEL_IN_STEREO
        // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
        var audioFormat = AudioFormat.ENCODING_PCM_16BIT
        // 缓冲区字节大小
        var bufferSizeInBytes = 0

        /**
         * 判断是是否有录音权限
         */
        fun isHasPermission(context: Context): Boolean {
            bufferSizeInBytes = 0
            bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                    channelConfig, audioFormat)
            var audioRecord: AudioRecord? = AudioRecord(audioSource, sampleRateInHz,
                    channelConfig, audioFormat, bufferSizeInBytes)
            //开始录制音频
            try {
                // 防止某些手机崩溃，例如联想
                audioRecord!!.startRecording()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }

            /**
             * 根据开始录音判断是否有录音权限
             */
            if (audioRecord!!.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
                return false
            }
            audioRecord.stop()
            audioRecord.release()
            audioRecord = null

            return true
        }
    }

}

