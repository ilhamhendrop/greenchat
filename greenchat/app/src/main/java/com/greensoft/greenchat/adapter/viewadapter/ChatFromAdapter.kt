package com.greensoft.greenchat.adapter.viewadapter

import com.greensoft.greenchat.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_chat_from.view.*

class ChatFromAdapter : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.item_chat_from
    }
}