package com.rezaharis.githubfavorites.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rezaharis.githubfavorites.R
import com.rezaharis.githubfavorites.databinding.ListFollowersBinding
import com.rezaharis.githubfavorites.entity.Followers
import java.lang.StringBuilder

class FollowersAdapter(private val listFollowers:ArrayList<Followers>): RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {

    fun setData(followers: ArrayList<Followers>){
        listFollowers.addAll(followers)
        notifyDataSetChanged()
    }

    inner class FollowersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListFollowersBinding.bind(itemView)

        fun bind(followers: Followers){
            Glide.with(itemView)
                    .load(followers.avatar_url)
                    .override(70,70)
                    .into(binding.avatarImage)
            binding.tvUsername.text = StringBuilder("@").append(followers.login)
            binding.tvName.text = followers.name
            binding.tvCompany.text = followers.company
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.list_followers, parent, false)
        return FollowersViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(listFollowers[position])
    }

    override fun getItemCount(): Int {
        return listFollowers.size
    }
}