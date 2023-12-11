package com.example.finalproject

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser: FirebaseUser? = firebaseAuth.currentUser

        // Mengambil informasi pengguna yang sedang login
        val userEmail = currentUser?.email
        val userId = currentUser?.uid

        // Menampilkan informasi pengguna di dalam aplikasi
        val userEmailTextView = findViewById<TextView>(R.id.userEmailTextView)
        val userIdTextView = findViewById<TextView>(R.id.userIdTextView)

        userEmailTextView.text = userEmail
        userIdTextView.text = userId
    }
}
