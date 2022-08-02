package com.sanjeet.innobuz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sanjeet.innobuz.R
import com.sanjeet.innobuz.model.PostItem


class DetailFragment : Fragment() {

    lateinit var postItem: PostItem
    var id: TextView? = null
    var userId: TextView? = null
    var title: TextView? = null
    var body: TextView? = null
    var imgBack: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postItem = it.getParcelable<PostItem>(HomeFragment.ARG_PARAM)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        container!!.removeAllViews()
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_detail, container, false)
        id = view.findViewById(R.id.tvId)
        userId = view.findViewById(R.id.tvUserId)
        title = view.findViewById(R.id.tvTitle)
        body = view.findViewById(R.id.tvbody)
        imgBack = view.findViewById(R.id.imgBack)

        id?.text = "Id : " + postItem.id.toString()
        userId?.text = "User Id : " + postItem.userId.toString()
        title?.text = "Title : " + postItem.title.toString()
        body?.text = "Description : " + postItem.body.toString()


        imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
        return view
    }


}