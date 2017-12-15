package com.aicyber.gathervoice

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(),TaskCenterFragment.OnFragmentInteractionListener,MyTaskFragment.OnFragmentInteractionListener,PersonCenterFragment.OnFragmentInteractionListener
{

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            window.enterTransition = Fade().setDuration(2000)
            window.exitTransition = Fade().setDuration(2000)
            toolbar.title = ""
            startService(Intent(this,MessageService::class.java))


            var fragmentList = ArrayList<Fragment>()
            var taskCenter = TaskCenterFragment()
            var myTask = MyTaskFragment()
            var personCenter = PersonCenterFragment()
            fragmentList!!.add(taskCenter)
            fragmentList!!.add(myTask)
            fragmentList!!.add(personCenter)

            main_viewpager!!.adapter = FrameFragmentPagerAdapter(this.supportFragmentManager, fragmentList)
            main_viewpager!!.currentItem = 0
            main_viewpager!!.setOnPageChangeListener(MainOnPageChangeListener())

            taskCenterButton.setOnClickListener {
                main_viewpager!!.currentItem = 0
            }

            myTaskButton.setOnClickListener {
                main_viewpager!!.currentItem = 1
            }

            personButton.setOnClickListener {
                main_viewpager!!.currentItem = 2
            }
        }
        catch (e:Exception)
        {
            println(e.message)
        }
    }

    fun resetBtn()
    {
        SetNormalColor(taskCenterImage,taskCenterText)
        SetNormalColor(myTaskImage,myTaskText)
        SetNormalColor(personImage,personText)
    }

    @SuppressLint("ResourceAsColor")
    fun SetNormalColor(image: ImageView?, text: TextView?)
    {
        image!!.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.gray))
        text!!.setTextColor(resources.getColor(R.color.gray))
    }

    @SuppressLint("ResourceAsColor")
    fun SetSelectColor(image: ImageView?, text: TextView?)
    {
        image!!.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.holo_blue_bright))
        text!!.setTextColor(resources.getColor(R.color.holo_blue_bright))
    }

    override fun onFragmentInteraction(uri: Uri) {
    }

    inner class MainOnPageChangeListener : ViewPager.OnPageChangeListener{
        override fun onPageSelected(position: Int) {
            resetBtn()
            when(position){
                0->{
                    SetSelectColor(taskCenterImage,taskCenterText)
                    title_text.text="任务中心"
                }
                1->{
                    SetSelectColor(myTaskImage,myTaskText)
                    title_text.text="我的任务"
                }
                2->{
                    SetSelectColor(personImage,personText)
                    title_text.text="个人中心"
                }
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageScrollStateChanged(state: Int) {
        }


    }

    inner class FrameFragmentPagerAdapter(fm: FragmentManager, var list: java.util.ArrayList<Fragment>) : FragmentPagerAdapter(fm) {

        override fun getItem(arg0: Int): Fragment {
            return list[arg0]
        }

        override fun getCount(): Int {
            return this.list.size
        }
    }
}

