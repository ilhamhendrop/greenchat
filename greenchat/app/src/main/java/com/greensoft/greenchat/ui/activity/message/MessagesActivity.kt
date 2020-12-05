package com.greensoft.greenchat.ui.activity.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.greensoft.greenchat.R
import com.greensoft.greenchat.adapter.model.ChatMessage
import com.greensoft.greenchat.adapter.model.User
import com.greensoft.greenchat.adapter.viewadapter.MessagesAdapter
import com.greensoft.greenchat.ui.activity.register.RegisterActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_messages.*
import java.lang.StringBuilder

class MessagesActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
    }

    private val listMessage = GroupAdapter<GroupieViewHolder>()
    private val latestMessage = HashMap<String, ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        setSupportActionBar(toolbar)
        supportActionBar?.title = StringBuilder("Message")

        verifyUserIsLoggedIn()
        fetchCurrentUser()
        loadMessage()
    }

    private fun loadMessage() {
        val fromId = FirebaseAuth.getInstance().uid
        val firebase = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")

        firebase.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return

                latestMessage[snapshot.key!!] = chatMessage
                refreshMessage()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return

                latestMessage[snapshot.key!!] = chatMessage
                refreshMessage()

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

        with(rvMessage){
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            adapter = listMessage
            addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
        }

        listMessage.setOnItemClickListener { item, view ->
            val message = item as MessagesAdapter

            val intent = Intent(applicationContext, ChatLogActivity::class.java).apply {
                putExtra(NewMessageActivity.USER_KEY, message.chatUser)
            }
            startActivity(intent)
        }
    }

    private fun refreshMessage() {
        listMessage.clear()
        latestMessage.values.forEach {
            listMessage.add(MessagesAdapter(it, applicationContext))
        }
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val firebase = FirebaseDatabase.getInstance().getReference("/users/$uid")

        firebase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid

        if (uid == null) {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_messages, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_signout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(applicationContext, RegisterActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_message -> {
                val intent = Intent(applicationContext, NewMessageActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}