package com.aicyber.gathervoice.page

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.aicyber.gathervoice.R
import com.aicyber.gathervoice.control.global
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_withdraw_cash_page.*
import java.text.DecimalFormat

class withdrawCashPage : AppCompatActivity() {
    private inner class RetultErrorMessge
    {
        var non_field_errors:Array<String>? = null
    }
    private inner class RetultMessge
    {
        var money:Array<String>? = null
    }

    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message?) {
            try {
                super.handleMessage(msg)
                when(msg!!.what)
                {
                    35->{
                        Toast.makeText(this@withdrawCashPage,"提现申请已提交！",Toast.LENGTH_LONG).show()
                    }
                    35->{
                        try {
                            var retData = msg.data.get("info").toString()
                            if(retData.indexOf("non_field_errors")>=0)
                            {
                                val errorInfo:RetultErrorMessge? = Gson().fromJson(retData,RetultErrorMessge::class.java)
                                Toast.makeText(this@withdrawCashPage,errorInfo?.non_field_errors?.get(0),Toast.LENGTH_LONG).show()
                            }
                            if(retData.indexOf("money")>=0)
                            {
                                val errorInfo:RetultMessge? = Gson().fromJson(retData,RetultMessge::class.java)
                                Toast.makeText(this@withdrawCashPage,errorInfo?.money?.get(0),Toast.LENGTH_LONG).show()
                            }

                        }
                        catch (e:Exception)
                        {
                            e.printStackTrace()
                        }
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw_cash_page)

        val cash = intent.getDoubleExtra("cash",0.0)
        var fm1 = DecimalFormat("#,###.00")
        my_cash.text = fm1.format(cash)
        withdraw_cash.setOnClickListener{
            global.withdrawCash(handler,cash)
        }
        backButton.setOnClickListener{
            finish()
        }
    }
}
