package com.iohardik.chatapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iohardik.chatapp.Activty.ChatActiviyy
import com.iohardik.chatapp.R
import com.iohardik.chatapp.databinding.ItemProfileBinding
import com.iohardik.chatapp.model.User

class UserAdapter(private val context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    inner class UserViewHolder(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.username.text = user.name
val imageuri="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQo23tYn4lKpHQfnMMHNWcf3pSMyg3wNQrJT2yFJSHA&s"

            Glide.with(context)

                .load(user.profileImage)
              .placeholder(R.drawable.profile_image)
                .dontAnimate()
                .into(binding.profile01)
            itemView.setOnClickListener{
                val intent = Intent(context,ChatActiviyy::class.java)
                intent.putExtra("name",user.name )
                intent.putExtra("image",user.profileImage)
                intent.putExtra("uid",user.uid)
                context.startActivity(intent)
            }
        }
    }
}
