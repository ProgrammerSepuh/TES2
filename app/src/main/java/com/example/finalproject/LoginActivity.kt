package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.finalproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

//class LoginActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityLoginBinding
//    private lateinit var firebaseAuth: FirebaseAuth
//
//    @SuppressLint("WrongViewCast")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        firebaseAuth = FirebaseAuth.getInstance()
//
//        binding.regisLink.setOnClickListener {
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
//        }
//
//        // ...
//        // Pada fungsi onCreate() atau fungsi lain yang sesuai
//// Inisialisasi komponen-komponen yang diperlukan
//        val editTextEmail = findViewById<EditText>(R.id.emailEt)
//        val editTextPassword = findViewById<EditText>(R.id.passET)
//        val buttonLogin = findViewById<Button>(R.id.btnlogin)
//
//// Tambahkan onClickListener ke tombol Login
//        buttonLogin.setOnClickListener {
//            val email = editTextEmail.text.toString().trim()
//            val password = editTextPassword.text.toString().trim()
//
//            if (email.isNotEmpty() && password.isNotEmpty()) {
//                // Proses login menggunakan Firebase Authentication
//                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            // Login berhasil
//                            Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this, ProfileActivity::class.java) // Ganti BerandaActivity dengan nama Activity selanjutnya
//                            startActivity(intent)
//                            finish()
//                            // Lanjutkan ke halaman beranda atau halaman profil
//                        } else {
//                            // Login gagal
//                            Toast.makeText(this, "Login gagal. Periksa kembali email dan password Anda.", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//            } else {
//                Toast.makeText(this, "Isi email dan password.", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//
//    }
//    }
class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val reg : TextView = findViewById(R.id.regis_link)
        reg.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        firebaseAuth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEt)
        val passwordEditText = findViewById<EditText>(R.id.passET)
        val loginButton = findViewById<Button>(R.id.btnlogin)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login berhasil, arahkan ke halaman profil
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Sebaiknya akhiri activity saat login berhasil
                        } else {
                            // Login gagal, tampilkan pesan kesalahan
                            Toast.makeText(this@LoginActivity, "Login gagal. Periksa kembali email dan password Anda.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Tampilkan pesan jika field email atau password kosong
                Toast.makeText(this@LoginActivity, "Isi email dan password.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

//    override fun onStart() {
//        super.onStart()
//        if (firebaseAuth.currentUser != null) {
//            val intent = Intent(this, com.example.finalproject.MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }

