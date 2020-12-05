package com.greensoft.greenchat.adapter.viewadapter

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.greensoft.greenchat.R
import com.greensoft.greenchat.adapter.model.ChatMessage
import com.greensoft.greenchat.adapter.model.User
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_messages.view.*


class MessagesAdapter(val message: ChatMessage, val context: Context) : Item<GroupieViewHolder>() {

    var chatUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtChat.text = message.chat

        val chatParnerId: String
        if(message.fromId == FirebaseAuth.getInstance().uid){
            chatParnerId = message.toId.toString()
        } else {
            chatParnerId = message.fromId.toString()
        }

        val firebase = FirebaseDatabase.getInstance().getReference("/users/$chatParnerId")
        firebase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatUser = snapshot.getValue(User::class.java)
                viewHolder.itemView.txtUsername.text = chatUser?.username
                Glide.with(context)
                        .load(chatUser?.photo)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_broken_image))
                        .error(R.drawable.ic_broken_image)
                        .into(viewHolder.itemView.imgPhoto)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun getLayout(): Int {
        return R.layout.item_messages
    }
}