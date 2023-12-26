package com.example.finalproject

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.InputStream

class UploadAdapter(
    private val context: Context,
    private val imageUris: MutableList<Uri>,
    private val onImageClickListener: OnImageClickListener // Tambahkan parameter interface
) : RecyclerView.Adapter<UploadAdapter.ImageViewHolder>() {

    // Interface untuk menangani klik pada gambar
    interface OnImageClickListener {
        fun onImageClick(imageUri: Uri)
    }

    // Kelas ViewHolder untuk menyimpan referensi tampilan
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUris[position]

        // Menggunakan InputStream dan BitmapFactory untuk memuat gambar dari Uri
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUrl)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        // Set Bitmap ke dalam ImageView
        holder.imageView.setImageBitmap(bitmap)

        // Tambahkan click listener pada gambar
        holder.imageView.setOnClickListener {
            onImageClickListener.onImageClick(imageUrl)
        }
    }

    override fun getItemCount(): Int {
        return imageUris.size
    }
}
