package com.example.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class AdapterHome(private val listKonten:List<KontenModel>):
    RecyclerView.Adapter<AdapterHome.ViewHolder>(){
    class ViewHolder (ItemView:View):RecyclerView.ViewHolder(ItemView){
        val profile:ImageView = itemView.findViewById(R.id.profileImage)
        val nama:TextView = itemView.findViewById(R.id.nameTv)
        val jam:TextView = itemView.findViewById(R.id.timeTv)
        val image:ImageView =  itemView.findViewById(R.id.imageKonten)
        val like:TextView = itemView.findViewById(R.id.jumlahLike)
        val komen:TextView = itemView.findViewById(R.id.jumlahKomen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.card_layout_home,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val KontenModel = listKonten[position]

        holder.profile.setImageResource(KontenModel.profile)
        holder.nama.text = KontenModel.nama
        holder.jam.text = KontenModel.jam
        holder.image.setImageResource(KontenModel.image)
        holder.like.text = KontenModel.like
        holder.komen.text = KontenModel.komen
    }

    override fun getItemCount(): Int {
        return listKonten.size
    }

}