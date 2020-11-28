package com.greensoft.greenchat.ui.activity.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.greensoft.greenchat.R
import com.greensoft.greenchat.adapter.model.User
import com.greensoft.greenchat.ui.activity.message.MessagesActivity
import com.greensoft.greenchat.ui.activity.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener(this)
        txtLogin.setOnClickListener(this)
        imgPhoto.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnRegister -> {
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


                                uploadImagePhoto()

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

            R.id.imgPhoto -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 0)
            }
        }
    }

    private fun uploadImagePhoto() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val firebase = FirebaseStorage.getInstance().getReference("/images/$filename")

        firebase.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Image", "Succes Image : ${it.metadata?.path}")

                    firebase.downloadUrl.addOnSuccessListener {
                        it.toString()
                        Log.d("Image", "File Location: $it")

                        saveUserDatabase(it.toString())
                    }

                }
    }

    private fun saveUserDatabase(photoUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val firbase = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val username = edUsername.text.toString()
        val user = User(uid, username, photoUrl)

        firbase.setValue(user)
            .addOnSuccessListener {
                edUsername.text.clear()
                edEmail.text.clear()
                edEmail.text.clear()
                edPassword.text.clear()

                val intent = Intent(applicationContext, MessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                Toast.makeText(applicationContext, "Succes ", Toast.LENGTH_SHORT).show()
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null ) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            imgPhoto2.setImageBitmap(bitmap)
            imgPhoto.alpha = 0f
        }
    }
}