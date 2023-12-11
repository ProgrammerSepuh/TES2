package com.example.finalproject
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        findViewById<TextView>(R.id.textView).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button).setOnClickListener {

            val email = findViewById<EditText>(R.id.emailEt).text.toString()
            val pass = findViewById<EditText>(R.id.passET).text.toString()
            val username = findViewById<EditText>(R.id.usernemeEt).text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && username.isNotEmpty()) {
                // Validate other conditions if needed

                // Create a user in Firebase Authentication
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            // Save user details to Realtime Database with username as the key
                            val userId = username.replace(".", "_") // Firebase doesn't allow dots in keys
                            val userRef = database.reference.child("users").child(userId)
                            userRef.child("email").setValue(email)

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, authTask.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed fffff!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
