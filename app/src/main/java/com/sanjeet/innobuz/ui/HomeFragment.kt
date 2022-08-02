package com.sanjeet.innobuz.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sanjeet.innobuz.R
import com.sanjeet.innobuz.dataBase.DatabaseHandler
import com.sanjeet.innobuz.model.PostItem
import com.sanjeet.innobuz.ui.adapter.PostAdapter
import com.sanjeet.maxtramachinetest.network.ApiClient
import com.sanjeet.maxtramachinetest.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {


    var postList = ArrayList<PostItem>()
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var adapter: PostAdapter
    lateinit var toptext: TextView
    lateinit var databaseHandler: DatabaseHandler
    var statusPref: String? = null
    var status: Long? = null
    lateinit var sp: SharedPreferences.Editor
    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = requireActivity().getPreferences(MODE_PRIVATE).edit()
        pref = requireActivity().getPreferences(MODE_PRIVATE)
        statusPref = pref.getString(Constant.STATUS, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        progressBar = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.rv_post)
        toptext = view.findViewById(R.id.tv_top)
        databaseHandler = DatabaseHandler(requireActivity())
        adapter = PostAdapter(postList, requireActivity())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        progressBar.visibility = View.VISIBLE


        getPostData()


        adapter.itemClickListener {
            val args = Bundle()
            args.putParcelable(ARG_PARAM, it)
            var mFragment: Fragment? = null
            mFragment = DetailFragment()
            mFragment.arguments = args
            val fragmentManager = requireActivity().supportFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.framlayout, mFragment)
                .commit()
        }



        return view
    }

    private fun getPostData() {
        val call: Call<List<PostItem>> = ApiClient.getClient.getPosts()
        call.enqueue(object : Callback<List<PostItem>> {
            override fun onResponse(
                call: Call<List<PostItem>>,
                response: Response<List<PostItem>>
            ) {
                response.body().let {
                    if (it != null) {
                        postList.addAll(it)
                        Log.e("=====", "Post size from Server : " + postList.size)

                        if (statusPref == null) {
                            for (i in postList) {
                                status = databaseHandler.addPost(i)
                            }
                            if (status!! > -1) {
                                sp.putString(Constant.STATUS, Constant.SAVED);
                                sp.apply()
                                progressBar.visibility = View.GONE
                                Toast.makeText(requireActivity(), "Record saved", Toast.LENGTH_LONG)
                                    .show()
                                getPostDataFromLocalDb();
                            } else {
                                progressBar.visibility = View.GONE
                                Toast.makeText(
                                    requireActivity(),
                                    "Something went wrong",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireActivity(),
                                "Records already saved into local DB",
                                Toast.LENGTH_SHORT
                            ).show()
                            getPostDataFromLocalDb();
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<PostItem>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getPostDataFromLocalDb() {
        progressBar.visibility = View.GONE
        postList = databaseHandler.viewPost();
        Log.e("=====", "Post size from local db : " + postList.size)
        adapter?.notifyDataSetChanged()
    }

    companion object {
        val ARG_PARAM = "detail"
    }

}