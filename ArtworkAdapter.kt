package com.example.artfinder.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.artfinder.R
import com.example.artfinder.model.Artwork

class ArtworkAdapter(
    private val items: List<Artwork>,
    private val onClick: (Artwork) -> Unit
) : RecyclerView.Adapter<ArtworkAdapter.ArtViewHolder>() {
    class ArtViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.artImage)
        val title: TextView = view.findViewById(R.id.artTitle)
        val artist: TextView = view.findViewById(R.id.artArtist)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ArtViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_artwork, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title ?: "Untitled"
        holder.artist.text = item.artist_display ?: "Unknown artist"
        Glide.with(holder.image).load(item.imageUrl).centerCrop().into(holder.image)
        holder.itemView.setOnClickListener { onClick(item) }
    }
}
