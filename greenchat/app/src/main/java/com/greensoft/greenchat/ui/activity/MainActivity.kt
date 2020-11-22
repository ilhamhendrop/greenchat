package com.greensoft.greenchat.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
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

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(applicationContext, "email/password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return
                }

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                edEmail.text.clear()
                                edPassword.text.clear()
                                Toast.makeText(applicationContext, "Succes ${it.result?.user?.email}", Toast.LENGTH_SHORT).show()
                                return@addOnCompleteListener
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
            }

            R.id.txtLogin -> {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }
    }
}