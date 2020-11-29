package com.greensoft.greenchat.adapter.viewadapter

import com.greensoft.greenchat.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatToAdapter : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getLayout(): Int {
        return R.layout.item_chat_to
    }
}