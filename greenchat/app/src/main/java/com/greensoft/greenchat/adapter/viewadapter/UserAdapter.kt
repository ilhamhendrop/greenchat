package com.greensoft.greenchat.adapter.viewadapter

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.greensoft.greenchat.R
import com.greensoft.greenchat.adapter.model.User
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(val user: User, private val context: Context) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtUser.text = user.username
        Glide.with(context)
            .load(user.photo)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_broken_image))
            .error(R.drawable.ic_broken_image)
            .into(viewHolder.itemView.imgPhoto)
    }

    override fun getLayout(): Int {
        return R.layout.item_user
    }

}