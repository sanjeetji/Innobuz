package com.sanjeet.innobuz.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sanjeet.innobuz.R
import com.sanjeet.innobuz.model.PostItem

class PostAdapter(private val postList: List<PostItem>, private val context: Context) :
    RecyclerView.Adapter<PostAdapter.MovieViewHolder>() {

    private var listener: ((PostItem) -> Unit)? = null
    var onItemLongClick: ((PostItem) -> Unit)?= null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostAdapter.MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(context).inflate(R.layout.post_item, parent, false)
        )
    }

    fun itemClickListener(l: (PostItem) -> Unit) {
        listener = l
    }
    fun itemLongClickListener(l: (PostItem) -> Unit) {
        onItemLongClick = l
    }

    override fun onBindViewHolder(holder: PostAdapter.MovieViewHolder, position: Int) {
        val postItem = postList.get(position)
        holder.postTitle.text = postItem.title

        holder.itemView.setOnClickListener {
            listener?.let {
                it(this.postList[position])
            }
        }

        holder.itemView.setOnLongClickListener{
            onItemLongClick?.let {
                it(this.postList[position])
            }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var postTitle: TextView

        init {
            postTitle = view.findViewById(R.id.post_title)
        }
    }

}