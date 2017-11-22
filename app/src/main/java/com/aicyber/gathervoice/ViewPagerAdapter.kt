package com.aicyber.gathervoice

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import java.util.ArrayList

/**
 * Created by donar on 2017/11/13.
 */
class ViewPagerAdapter(private val views: ArrayList<View>?) : PagerAdapter() {

    override fun getCount(): Int {
        return if (this.views != null) views.size else 0
    }

    override fun instantiateItem(view: View?, position: Int): Any {

        (view as ViewPager).addView(views!![position], 0)

        return views[position]
    }

    override fun isViewFromObject(view: View, arg1: Any): Boolean {
        return view === arg1
    }

    override fun destroyItem(view: View?, position: Int, arg2: Any?) {
        (view as ViewPager).removeView(views!![position])
    }

}
