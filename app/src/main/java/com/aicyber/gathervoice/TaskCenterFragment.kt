package com.aicyber.gathervoice

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TabHost
import com.aicyber.gathervoice.data.TaskInfo
import kotlinx.android.synthetic.main.fragment_task_center.*
import kotlinx.android.synthetic.main.task_card.*
import kotlinx.android.synthetic.main.task_card.view.*


@Suppress("DEPRECATION")
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TaskCenterFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TaskCenterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskCenterFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var isInit = false
    private var adapter:TaskViewAdapter? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        GetVoiceTaskList()
    }
    private fun GetVoiceTaskList() {
        adapter!!.tasks.clear()
        adapter!!.listType = adapter!!.VoiceTypeList
        var i:Int = 0
        while ( i< 10)
        {
            var taskInfo = TaskInfo()
            taskInfo.name = "语音任务" + (i+1)
            adapter!!.tasks.add(taskInfo)
            i++
        }
        //重置滑动位置
        list_view.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }

    private fun GetCheckTaskList(){
        adapter!!.tasks.clear()
        adapter!!.listType = adapter!!.CheckTypeList
        var i:Int = 0
        while ( i< 10)
        {
            var taskInfo = TaskInfo()
            taskInfo.name = "校对任务" + (i+1)
            adapter!!.tasks.add(taskInfo)
            i++
        }
        list_view.adapter = adapter
        adapter!!.notifyDataSetChanged()

    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_task_center, container, false)
    }

    var currentView:View? = null
    public fun init()
    {
        val tasks:ArrayList<TaskInfo> = ArrayList()
        try {
            if(!isInit)
                adapter = TaskViewAdapter(view!!.context, tasks)
            list_view.adapter = adapter
            list_view.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->run{
                currentView = view1
                var task:TaskInfo = (view1.tag as TaskInfo?)!!
                val bundle = Bundle()
                bundle.putParcelable("data",task)
                val intent = Intent(view!!.context, com.aicyber.gathervoice.Page.TaskInfoPage::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
                }
                this.isInit = true
            }
            tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.text)
                    {
                        "语音任务"->GetVoiceTaskList()
                        "校对任务"->GetCheckTaskList()
                    }
                }

            })
        }
        catch (e:Exception)
        {
            val message = e.message;
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode ){
            1->currentView!!.transitionName = ""
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskCenterFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): TaskCenterFragment {
            val fragment = TaskCenterFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor


