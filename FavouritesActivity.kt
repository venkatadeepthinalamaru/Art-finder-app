package com.example.artfinder.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.artfinder.R
import com.example.artfinder.data.AppDatabase
import com.example.artfinder.model.Artwork
import com.example.artfinder.ui.adapter.ArtworkAdapter
import kotlinx.coroutines.launch

class FavouritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        val recycler = findViewById<RecyclerView>(R.id.favouritesRecycler)
        recycler.layoutManager = GridLayoutManager(this, 2)
        lifecycleScope.launch {
            try {
                val saved = AppDatabase.get(this@FavouritesActivity).artworkDao().getAll()
                val items = saved.map {
                    Artwork(it.id, it.title, it.artist, it.date, it.imageId)
                }
                recycler.adapter = ArtworkAdapter(items) { item ->
                    startActivity(Intent(this@FavouritesActivity, DetailsActivity::class.java).apply {
                        putExtra("id", item.id)
                        putExtra("title", item.title.orEmpty())
                        putExtra("artist", item.artist_display.orEmpty())
                        putExtra("date", item.date_display.orEmpty())
                        putExtra("imageId", item.image_id.orEmpty())
                    })
                }
                if (items.isEmpty()) Toast.makeText(this@FavouritesActivity, "No favourites saved yet", Toast.LENGTH_SHORT).show()
            } catch (_: Exception) {
                Toast.makeText(this@FavouritesActivity, "Could not load favourites", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
