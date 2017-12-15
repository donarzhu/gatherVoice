package com.aicyber.gathervoice

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.aicyber.gathervoice.page.checkTaskPage
import com.aicyber.gathervoice.page.voiceTaskListPage
import com.aicyber.gathervoice.control.global
import com.aicyber.gathervoice.data.MyTaskInfo
import com.aicyber.gathervoice.data.MyVerityTask
import com.aicyber.gathervoice.data.TaskInfo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_my_task.*
@Suppress("DEPRECATION")
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MyTaskFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MyTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyTaskFragment : Fragment() {
    private inner class GetMyTasksResult{
        var count:Int = 0
        var next:String?=null
        var previous:String?=null
        var results:Array<MyTaskInfo>?=null
    }
    private  inner  class GetMyVerifyListResult{
        var count:Int = 0
        var next:String?=null
        var previous:String?=null
        var results:Array<MyVerityTask>?=null
    }
    private var myTasks:Array<MyTaskInfo>?=null
    private var myVeritys:Array<MyVerityTask>?=null

    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            try {
                when(msg!!.what)
                {
                    10->{
                        var retData = msg.data.get("info").toString()
                        val retMsg: GetMyVerifyListResult? = Gson().fromJson(retData, GetMyVerifyListResult::class.java)
                        if(retMsg!=null && retMsg!!.results!=null)
                        {
                            myVeritys = retMsg!!.results
                            for(task:MyVerityTask in retMsg!!.results!!)
                                adapter!!.tasks.add(task.todo!!.task!!)
                        }
                        my_tasks.adapter = adapter
                        adapter!!.notifyDataSetChanged()
                    }
                    9->{
                        var retData = msg.data.get("info").toString()
                        val retMsg: GetMyTasksResult? = Gson().fromJson(retData, GetMyTasksResult::class.java)
                        if(retMsg!=null && retMsg!!.results!=null)
                        {
                            myTasks = retMsg!!.results
                            for(task:MyTaskInfo in retMsg!!.results!!)
                                adapter!!.tasks.add(task.task!!)
                        }
                        my_tasks.adapter = adapter
                        adapter!!.notifyDataSetChanged()

                    }

                }
            }
            catch (e:Exception)
            {
                println(e.message)
            }
        }
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_my_task, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        GetVoiceTaskList()
    }


    var currentView:View? = null
    var isInit = false
    private var listType =  0
    private var adapter:TaskViewAdapter? = null
    public fun init()
    {
        val tasks:ArrayList<TaskInfo> = ArrayList()
        try {
            if(!isInit)
                adapter = TaskViewAdapter(view!!.context, tasks,1)
            my_tasks.adapter = adapter
            my_tasks.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->run{
                currentView = view1
                var task:TaskInfo = (view1.tag as TaskInfo?)!!
                val bundle = Bundle()
                bundle.putParcelable("data",task)
                when(listType)
                {
                    0->{
                        bundle.putInt("id",myTasks!![i].id)
                        goToVoicePage(bundle)
                    }
                    1->{
                        bundle.putInt("id",myVeritys!![i].id)
                        goToCheckPage(bundle)
                    }
                }
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

    private fun goToVoicePage(bundle: Bundle)
    {
        val intent = Intent(view!!.context, voiceTaskListPage::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private  fun goToCheckPage(bundle: Bundle)
    {
        val intent = Intent(view!!.context, checkTaskPage::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun GetVoiceTaskList() {
        listType=0
        adapter!!.tasks.clear()
        adapter!!.listType = adapter!!.VoiceTypeList
        global.getMyTaskList(handler)
        /*var i:Int = 0
        while ( i< 10)
        {
            var taskInfo = TaskInfo()
            taskInfo.name = "语音任务" + (i+1)
            adapter!!.tasks.add(taskInfo)
            i++
        }
        //重置滑动位置
        my_tasks.adapter = adapter
        adapter!!.notifyDataSetChanged()*/
    }

    private fun GetCheckTaskList(){
        listType=1
        adapter!!.tasks.clear()
        adapter!!.listType = adapter!!.CheckTypeList
        global.getMyVerifyList(handler)
        /*
        var i:Int = 0
        while ( i< 10)
        {
            var taskInfo = TaskInfo()
            taskInfo.name = "校对任务" + (i+1)
            adapter!!.tasks.add(taskInfo)
            i++
        }*/

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
         * @return A new instance of fragment MyTaskFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): MyTaskFragment {
            val fragment = MyTaskFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
