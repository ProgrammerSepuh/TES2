package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class DetailImageActivity : AppCompatActivity() {

    private lateinit var imageViewDetail: ImageView
    private lateinit var textViewDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_image)

        imageViewDetail = findViewById(R.id.imageViewDetail)
        textViewDescription = findViewById(R.id.textViewDescription)

        val imageUrl = intent.getStringExtra("imageUrl")
        val description = intent.getStringExtra("imageDescription")

        // Tampilkan gambar pada ImageView secara penuh
        Glide.with(this)
            .load(imageUrl)
            .into(imageViewDetail)

        // Tampilkan deskripsi gambar pada TextView
        textViewDescription.text = description
    }
}