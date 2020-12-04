package com.greensoft.greenchat.ui.activity.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.greensoft.greenchat.R
import com.greensoft.greenchat.adapter.model.ChatMessage
import com.greensoft.greenchat.adapter.model.User
import com.greensoft.greenchat.adapter.viewadapter.ChatFromAdapter
import com.greensoft.greenchat.adapter.viewadapter.ChatToAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val TAG = "ChatLog"
    }

    private val listChat = GroupAdapter<GroupieViewHolder>()
    private var toUser : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        toUser = intent.getParcelableExtra(NewMessageActivity.USER_KEY)

        setSupportActionBar(toolbar)
        supportActionBar?.title = toUser?.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnKirim.setOnClickListener(this)

        loadChat()
    }

    private fun loadChat() {
        val firebase = FirebaseDatabase.getInstance().getReference("/messages")

        firebase.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.chat)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        val fromUser = MessagesActivity.currentUser ?: return
                        listChat.add(ChatFromAdapter(chatMessage.chat.toString(), fromUser, applicationContext))
                    } else {
                        listChat.add(ChatToAdapter(chatMessage.chat.toString(), toUser!!, applicationContext ))
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        with(rvChat) {
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            adapter = listChat
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnKirim -> sendMessage()
        }
    }

    private fun sendMessage() {
        val chat = edChat.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        if (fromId == null) return

        val firebase = FirebaseDatabase.getInstance().getReference("/messages").push()
        val chatMessage = ChatMessage(firebase.key, chat, fromId, toId, System.currentTimeMillis() / 1000)
        firebase.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG, "Succes our chat messages: ${firebase.key}")
                    edChat.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT).show()
                }
    }
}