package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // ... (existing code)

        binding.regisLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // ...
        binding.btnlogin.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
        //asasjajsajsjasj
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener { authResult ->
                        // Mengakses informasi pengguna yang login
                        val user = authResult.user
                        val userEmail = user?.email // Mendapatkan alamat email pengguna

                        // Menyiapkan intent untuk membawa informasi pengguna ke ProfileActivity
                        val intent = Intent(this, ProfileActivity::class.java)
                        intent.putExtra("user_email", userEmail) // Mengirim data email pengguna ke ProfileActivity
                        startActivity(intent)

                        // Optional: Menutup LoginActivity setelah berhasil login
                        finish()
                    }
                    .addOnFailureListener { e ->
                        // Penanganan kesalahan jika login gagal
                        Toast.makeText(this, "Login gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        }
// ...

    }

//    override fun onStart() {
//        super.onStart()
//        if (firebaseAuth.currentUser != null) {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }

