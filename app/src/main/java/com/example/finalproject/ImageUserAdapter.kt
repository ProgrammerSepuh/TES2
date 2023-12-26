package com.example.finalproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ImageUserAdapter(
    private val context: Context,
    private val imageList: List<String>,
    private val descriptionList: List<String>
) :
    RecyclerView.Adapter<ImageUserAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageList[position]
        val description = descriptionList[position] // Menambahkan baris ini

        holder.bind(imageUrl, description) // Menyesuaikan panggilan metode bind
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewItem)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription) // Menambahkan baris ini

        fun bind(imageUrl: String, description: String) {
            Picasso.get().load(imageUrl).into(imageView)
            descriptionTextView.text = description
        }
    }
}
