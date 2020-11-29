package com.greensoft.greenchat.ui.activity.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greenchat.R
import com.greensoft.greenchat.adapter.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import java.lang.StringBuilder

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        setSupportActionBar(toolbar)
        supportActionBar?.title = user?.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        loadChat()
    }

    private fun loadChat() {

        val listChat = GroupAdapter<GroupieViewHolder>()

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
}