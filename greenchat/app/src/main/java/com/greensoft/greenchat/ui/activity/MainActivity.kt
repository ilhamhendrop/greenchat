package com.greensoft.greenchat.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.greensoft.greenchat.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnRegister.setOnClickListener(this)
        txtLogin.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnRegister -> {
                val username = edPassword.text.toString()
                val email = edEmail.text.toString()
                val password = edPassword.text.toString()
            }

            R.id.txtLogin -> {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }
    }
}