package com.aicyber.gathervoice.page

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.ArrayAdapter
import android.widget.Toast
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import kotlinx.android.synthetic.main.activity_binding_card_page.*

class BindingCardPage : AppCompatActivity() {
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            when(msg?.what)
            {
                37-> Toast.makeText(this@BindingCardPage,"绑定完成",Toast.LENGTH_LONG).show()
                38-> Toast.makeText(this@BindingCardPage,"输入信息有误，请检查后再试！",Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binding_card_page)
        var lagArray:Array<String> = global.getBankArray()
        var adapter: ArrayAdapter<String> = ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,lagArray)
        bank_name_Spinner.adapter = adapter

        BindingButton.setOnClickListener {
            var pwd = inputPwd.text.toString()
            var id_no = inputIDNumber.text.toString()
            var card = inputCard.text.toString()
            var bank = bank_name_Spinner.selectedItem.toString()
            var name = inputUserName.text.toString()
            if(pwd.length < 6)
            {
                Toast.makeText(this,"输入密码太短，请重新输入",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(id_no.length != 18)
            {
                Toast.makeText(this,"身份证号输入错误",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(bank.length < 4)
            {
                Toast.makeText(this,"银行名称输入有误",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(card.length < 10)
            {
                Toast.makeText(this,"银行卡号输入有误",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(name.length<2)
            {
                Toast.makeText(this,"姓名错误",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            global.bindingHankCard(handler,pwd,name,id_no,bank,card)
        }
        backButton.setOnClickListener{
            finish()
        }
    }
}
