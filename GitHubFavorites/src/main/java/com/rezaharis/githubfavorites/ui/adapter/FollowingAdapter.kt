package com.rezaharis.githubfavorites.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rezaharis.githubfavorites.R
import com.rezaharis.githubfavorites.databinding.ListFollowingBinding
import com.rezaharis.githubfavorites.entity.Following
import java.lang.StringBuilder

class FollowingAdapter(private val listFollowing:ArrayList<Following>): RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    fun setData(following: ArrayList<Following>){
        listFollowing.addAll(following)
        notifyDataSetChanged()
    }

    inner class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListFollowingBinding.bind(itemView)

        fun bind(following: Following){
            Glide.with(itemView)
                    .load(following.avatar_url)
                    .override(80,80)
                    .into(binding.avatarImage)

            binding.tvUsername.text = StringBuilder("@").append(following.login)
            binding.tvName.text = following.name
            binding.tvCompany.text = following.company
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.list_following, parent, false)
        return FollowingViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(listFollowing[position])
    }

    override fun getItemCount(): Int {
        return listFollowing.size
    }
}