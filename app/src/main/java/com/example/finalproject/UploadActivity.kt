package com.example.finalproject

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.model.Upload
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadActivity : AppCompatActivity(), UploadAdapter.OnImageClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var uploadAdapter: UploadAdapter
    private lateinit var selectedImageUris: MutableList<Uri>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private var currentSelectedImageUri: Uri? = null

    companion object {
        private const val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        firebaseAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference.child("images")

        recyclerView = findViewById(R.id.recyclerView)
        selectedImageUris = mutableListOf()

        // Ganti UploadAdapter menjadi UploadAdapter
        uploadAdapter = UploadAdapter(this, selectedImageUris, this)

        recyclerView.apply {
            layoutManager = GridLayoutManager(this@UploadActivity, 3)
            adapter = uploadAdapter
        }

        val buttonUpload: Button = findViewById(R.id.buttonUpload)
        buttonUpload.setOnClickListener {
            uploadFiles()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_EXTERNAL_STORAGE_PERMISSION_CODE
                )
            } else {
                loadImagesFromGallery()
            }
        } else {
            loadImagesFromGallery()
        }
    }

    private fun loadImagesFromGallery() {
        val imageProjection = arrayOf(MediaStore.Images.Media.DATA)
        val imageCursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            null
        )

        imageCursor?.let {
            selectedImageUris.clear()
            while (it.moveToNext()) {
                val imagePath = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                val imageUri = Uri.parse("file://$imagePath")
                Log.d("ImagePath", "Image path: $imageUri")
                selectedImageUris.add(imageUri)
            }
            it.close()

            uploadAdapter.notifyDataSetChanged()
        }
    }

    private fun uploadFiles() {
        if (currentSelectedImageUri != null) {
            val editTextDescription: EditText = findViewById(R.id.editTextDescription)
            val descriptionText: String = editTextDescription.text.toString()

            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val fileReference = storageReference.child("${System.currentTimeMillis()}_${userId}")

                fileReference.putFile(currentSelectedImageUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        fileReference.downloadUrl.addOnSuccessListener { uri ->
                            val uploadData = Upload(
                                imageUrl = uri.toString(),
                                description = descriptionText,
//                                userId = userId ?: ""
                            )

                            // Save image data to Firebase Database (example using Realtime Database)
                            val databaseReference =
                                FirebaseDatabase.getInstance().reference.child("uploads")
                            val uploadId = databaseReference.push().key
                            if (uploadId != null) {
                                databaseReference.child(uploadId).setValue(uploadData)
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Failed to upload image: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                // Clear the current selected image URI
                currentSelectedImageUri = null

                // Clear the ImageView
                val imageView: ImageView = findViewById(R.id.imageView)
                imageView.setImageResource(android.R.drawable.ic_menu_gallery)

                // Show a toast or perform any action after upload
                Toast.makeText(this, "Upload berhasil", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "User ID tidak valid", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Tidak ada file yang dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImagesFromGallery()
            } else {
                Toast.makeText(this, "Izin akses galeri ditolak", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onImageClick(imageUri: Uri) {
        // Set the current selected image URI
        currentSelectedImageUri = imageUri

        // Update the ImageView with the selected image
        val imageView: ImageView = findViewById(R.id.imageView)
        imageView.setImageURI(imageUri)
    }
}
