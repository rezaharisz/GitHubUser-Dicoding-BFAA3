package com.rezaharis.githubuserapp2.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rezaharis.githubuserapp2.R
import com.rezaharis.githubuserapp2.databinding.ListFavoritesBinding
import com.rezaharis.githubuserapp2.entity.User
import com.rezaharis.githubuserapp2.ui.view.activity.UserDetail
import java.lang.StringBuilder

class FavoriteAdapter(private val activity: Activity): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var user = ArrayList<User>()
        set(listUser){
            if (listUser.size > 0){
                this.user.clear()
            }
            this.user.addAll(listUser)
            notifyDataSetChanged()
        }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListFavoritesBinding.bind(itemView)

        fun bind(user: User){

            Glide.with(itemView)
                    .load(user.avatar_url)
                    .override(70,70)
                    .into(binding.avatarImage)
            binding.tvUsername.text = StringBuilder("@").append(user.login)
            binding.tvName.text = user.name
            binding.tvCompany.text = user.company

            itemView.setOnClickListener {
                val intent = Intent(activity, UserDetail::class.java).also {
                    it.putExtra(UserDetail.USER, user)
                }
                activity.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_favorites, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(user[position])
    }

    override fun getItemCount(): Int {
        return user.size
    }
}