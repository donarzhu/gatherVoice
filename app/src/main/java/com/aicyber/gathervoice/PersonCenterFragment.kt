package com.aicyber.gathervoice

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.aicyber.gathervoice.Page.ChangeUserInfoPage
import com.aicyber.gathervoice.control.global
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_person_center.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PersonCenterFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PersonCenterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonCenterFragment : Fragment() {
    private inner class ResultUserInfo{
        var id = 0
        var username = ""
        var tel = ""
        var tel_bind_at:String? = null
        var sex:String? = null
        var age:Int? = null
        var place:String? = null
        var credit=0
        var reward=0.0
        var wx_openId:String?= null
        var wx_nickName:String?= null
        var wx_avatarUrl:String? = null
        var wx_gender:String? = null
        var wx_province:String? = null
        var wx_country:String?= null
        var wx_bind_at:String? = null
        var wx_login_at:String? = null
    }

    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            try {
                var retData = msg!!.data.get("info").toString()
                val retMsg: ResultUserInfo = Gson().fromJson(retData, ResultUserInfo::class.java) ?: return
                if(!retMsg.username.isNullOrEmpty())
                    user_name.text = retMsg.username
                if(!retMsg.sex.isNullOrEmpty())
                    user_sex.text = retMsg.sex
                if(retMsg.age!=null)
                    user_age.text = retMsg.age.toString()
                if(!retMsg.place.isNullOrEmpty())
                    user_place.text = retMsg.place
                user_credit.text = retMsg.credit.toString()
                user_reward.text = retMsg.reward.toString()
                if(!retMsg.tel.isNullOrEmpty())
                    user_tel.text = retMsg.tel
            }
            catch (e:Exception)
            {
                e.printStackTrace()
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
        return inflater!!.inflate(R.layout.fragment_person_center, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_back.background.alpha = 100
        global.getUserInfo(handler)
        update_info.setOnClickListener{
            var intent = Intent(view!!.context,ChangeUserInfoPage::class.java)
            global.sendDataHandler = handler
            startActivity(intent)
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
         * @return A new instance of fragment PersonCenterFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): PersonCenterFragment {
            val fragment = PersonCenterFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
