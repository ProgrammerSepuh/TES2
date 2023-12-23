//package com.example.finalproject
//
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.auth.UserProfileChangeRequest
//
//class EditProfileActivity : AppCompatActivity() {
//
//    private lateinit var firebaseAuth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_edit_profile)
//
//        firebaseAuth = FirebaseAuth.getInstance()
//        val currentUser: FirebaseUser? = firebaseAuth.currentUser
//
//        val userEmailEditText = findViewById<EditText>(R.id.editEmail)
//        val usernameEditText = findViewById<EditText>(R.id.editUsername)
//        val saveButton = findViewById<Button>(R.id.buttonUpdate)
//
//        saveButton.setOnClickListener {
//            val newEmail = userEmailEditText.text.toString()
//            val newUsername = usernameEditText.text.toString()
//
//            if (newEmail.isNotEmpty()) {
//                currentUser?.updateEmail(newEmail)
//                    ?.addOnCompleteListener { updateEmailTask ->
//                        if (updateEmailTask.isSuccessful) {
//                            Toast.makeText(
//                                this,
//                                "Email updated successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            // Lakukan aksi lanjutan setelah pengubahan email berhasil (jika ada)
//                        } else {
//                            Toast.makeText(
//                                this,
//                                "Failed to update email",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//            }
//
//            if (newUsername.isNotEmpty()) {
//                val profileUpdates = UserProfileChangeRequest.Builder()
//                    .setDisplayName(newUsername)
//                    .build()
//
//                currentUser?.updateProfile(profileUpdates)
//                    ?.addOnCompleteListener { updateProfileTask ->
//                        if (updateProfileTask.isSuccessful) {
//                            Toast.makeText(
//                                this,
//                                "Username updated successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            // Lakukan aksi lanjutan setelah pengubahan username berhasil (jika ada)
//                        } else {
//                            Toast.makeText(
//                                this,
//                                "Failed to update username",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//            }
//
//            finish() // Kembali ke halaman profil setelah perubahan berhasil
//        }
//    }
//}

package com.example.finalproject
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid // Mendapatkan UID pengguna saat ini

        userId?.let { uid ->
            userReference = database.reference.child("users").child(uid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val username = snapshot.child("username").value.toString()

                        // Mengisi EditText dengan data saat ini dari Firebase
                        findViewById<EditText>(R.id.editUsername).setText(username)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error saat membaca data dari Firebase
                    Toast.makeText(this@EditProfileActivity, "Failed to read user data.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        val btnSave: Button = findViewById(R.id.buttonUpdate)
        btnSave.setOnClickListener {
            val updatedUsername = findViewById<EditText>(R.id.editUsername).text.toString()

            // Memperbarui data username di Firebase
            userReference.child("username").setValue(updatedUsername)
                .addOnSuccessListener {
                    Toast.makeText(this@EditProfileActivity, "Username berhasil diperbarui.", Toast.LENGTH_SHORT).show()
                    finish() // Menutup activity setelah berhasil menyimpan perubahan
                }
                .addOnFailureListener {
                    Toast.makeText(this@EditProfileActivity, "Gagal memperbarui username.", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
