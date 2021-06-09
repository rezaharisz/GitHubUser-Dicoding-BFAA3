package com.rezaharis.githubuserapp2.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rezaharis.githubuserapp2.R
import com.rezaharis.githubuserapp2.ui.view.activity.UserDetail
import com.rezaharis.githubuserapp2.databinding.ListUserBinding
import com.rezaharis.githubuserapp2.entity.User
import java.lang.StringBuilder

class UserAdapter(private val listUser:ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    fun setData(user: ArrayList<User>){
        listUser.addAll(user)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val binding = ListUserBinding.bind(itemView)

        fun bind(user: User){
            with(itemView){
                Glide.with(itemView)
                        .load(user.avatar_url)
                        .override(80,80)
                        .into(binding.avatarImage)
                binding.tvUsername.text = StringBuilder("@").append(user.login)
                binding.tvName.text = user.name
                binding.tvCompany.text = user.company

                itemView.setOnClickListener {
                    val intent = Intent(this.context, UserDetail::class.java).also {
                        it.putExtra(UserDetail.USER, user)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.list_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

}