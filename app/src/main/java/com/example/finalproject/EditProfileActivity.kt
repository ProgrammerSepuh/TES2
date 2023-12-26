package com.example.finalproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class EditProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var profileImage: CircleImageView
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        try {
            // Kode yang mungkin menyebabkan crash
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
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Failed to read user data.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

            profileImage = findViewById(R.id.profileImage1)
            val btnSave: Button = findViewById(R.id.buttonUpdate)
            btnSave.setOnClickListener {
                val updatedUsername = findViewById<EditText>(R.id.editUsername).text.toString()

                // Memperbarui data username di Firebase
                userReference.child("username").setValue(updatedUsername)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Username berhasil diperbarui.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Menutup activity setelah berhasil menyimpan perubahan
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Gagal memperbarui username.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            val editFotoImageView: ImageView = findViewById(R.id.editFoto)
            editFotoImageView.setOnClickListener {
                openGallery()
            }
        } catch (e: Exception) {
            // Tangani kesalahan di sini
            e.printStackTrace()
            Toast.makeText(
                this@EditProfileActivity,
                "Terjadi kesalahan: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    data?.data?.let { uri ->
                        selectedImageUri = uri
                        profileImage.setImageURI(uri)
                        // Panggil fungsi untuk mengunggah foto profil ke Firebase Storage dan menyimpan URL di database
                        uploadProfileImage(uri)
                    }
                }
            }
        }
    }

    private fun uploadProfileImage(uri: Uri) {
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val userId: String? = currentUser?.uid

        userId?.let { uid ->
            val storageReference: StorageReference = FirebaseStorage.getInstance().reference
                .child("profile_images")
                .child("$uid.jpg")

            storageReference.putFile(uri)
                .addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                        userReference.child("profileImageUrl").setValue(downloadUri.toString())
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal mengunggah gambar profil.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        private const val REQUEST_GALLERY = 1
    }
}
