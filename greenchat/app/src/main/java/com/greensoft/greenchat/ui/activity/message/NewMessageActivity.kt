package com.greensoft.greenchat.ui.activity.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.greensoft.greenchat.R
import com.greensoft.greenchat.adapter.model.User
import com.greensoft.greenchat.adapter.viewadapter.UserAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import java.lang.StringBuilder

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        setSupportActionBar(toolbar)
        supportActionBar?.title = StringBuilder("Message")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadUser()

    }

    private fun loadUser() {
        val firebase = FirebaseDatabase.getInstance().getReference("/users")
        firebase.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val listUser = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        listUser.add(UserAdapter(user, applicationContext))
                    }
                }

                with(rvUser){
                    adapter = listUser
                    layoutManager = LinearLayoutManager(applicationContext)
                    setHasFixedSize(true)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}