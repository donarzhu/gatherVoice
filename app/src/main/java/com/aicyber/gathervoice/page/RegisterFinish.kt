package com.aicyber.gathervoice.page

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register_finish.*
import android.widget.RadioButton
import android.widget.AdapterView.OnItemSelectedListener
import kotlinx.android.synthetic.main.activity_register_phone.*
import kotlinx.android.synthetic.main.fragment_person_center.*


class RegisterFinish : AppCompatActivity() {
    private inner class RetultError
    {
        var error:String = ""
    }
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message?) {
            try {
                super.handleMessage(msg)
                when(msg!!.what)
                {
                    3->
                    {
                        Toast.makeText(this@RegisterFinish,"注册成功，请返回登录！", Toast.LENGTH_SHORT).show()
                        //global.updateUserInfo(this,sex,age,place)
                    }
                    4->
                    {
                        var retData = msg.data.get("info").toString()
                        val retMsg:RetultError? = Gson().fromJson(retData,RetultError::class.java)
                        if(retMsg!=null)
                            Toast.makeText(this@RegisterFinish,retMsg.error, Toast.LENGTH_SHORT).show()
                    }
                }

            }
            catch (e:Exception)
            {
                var msg = e.message
                println(msg)
            }
        }
    }

    var sex = ""
    var age = 0
    var place =""
    var userName = ""
    val selectDialect:MutableList<Int> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_finish)
        val phone = intent.getStringExtra("user")
        val pwd = intent.getStringExtra("password")
        val vCode = intent.getStringExtra("code")

        var lagArray:Array<String> = global.getDialectArray()
        var adapter:ArrayAdapter<String> = ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,lagArray)
        lag_spinner.adapter = adapter
        lag_spinner2.adapter = adapter
        //预设 spinner.setSelection(2,true);
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val rb = findViewById<RadioButton>(checkedId)
            sex = rb.text.toString()
        }

        backRegButton.setOnClickListener{
            this.finish()
        }
        registerButton.setOnClickListener{
            try {

                if (sex.isNullOrEmpty()) {
                    Toast.makeText(this@RegisterFinish , "请选择性别!" , Toast.LENGTH_SHORT)
                    return@setOnClickListener
                }
                var intSex = 1
                when (sex) {
                    "男" -> intSex = 1
                    "女" -> intSex = 2
                }
                age = inputAge.text.toString().toInt()
                if (age < 10 || age > 60) {
                    Toast.makeText(this@RegisterFinish , "请输入正确的年龄!" , Toast.LENGTH_SHORT)
                    return@setOnClickListener
                }
                userName = inputUserName.text.toString()
                if (userName.isNullOrEmpty()) {
                    Toast.makeText(this@RegisterFinish , "请输入姓名!" , Toast.LENGTH_SHORT)
                    return@setOnClickListener
                }
                var dialect1 = lag_spinner.selectedItem.toString()
                var dialect2 = lag_spinner2.selectedItem.toString()
                var v1 = global.getDialectID(dialect1)!!
                selectDialect.add(v1)
                if (dialect1 != dialect2) {
                    var v2 = global.getDialectID(dialect2)!!
                    selectDialect.add(v2)
                }
                global.register(handler , phone , pwd , age , intSex , vCode , selectDialect.toTypedArray() , userName)
            }
            catch (e:Exception)
            {
                e.printStackTrace()
            }
        }
    }
}
