package com.aicyber.gathervoice.control

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.os.Environment.getExternalStorageDirectory



/**
 * Created by donar on 2017/11/22.
 */
class AudioPlayFunc private constructor(){

    companion object {
        val instance = AudioPlayFunc()
    }

    fun getRingDuring(context: Context, mUri: String?): String? {
        var duration: String? = null
        val mediaPlayer = MediaPlayer()
        try {
            var file: File = File(mUri)
            if(!file.exists())
                return null
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            var date = Date(mediaPlayer.duration.toLong())
            val formatter = SimpleDateFormat("mm:ss")
            duration = formatter.format(date)
        }
        catch (e:Exception)
        {
            Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
        }
        finally {
            mediaPlayer.release()
        }
        return duration
    }

    var mediaPlayer:MediaPlayer? = null
    fun playAudio(context: Context, mUri: String?)
    {
       val file = File(mUri)

        try {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(mUri))
            mediaPlayer!!.start()
            mediaPlayer!!.setOnCompletionListener {
                Toast.makeText(context,"播放完毕", Toast.LENGTH_SHORT).show()
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            mediaPlayer!!.release()
            mediaPlayer = null
        }

    }
}