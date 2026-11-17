package com.example.artfinder.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.artfinder.R
import com.example.artfinder.model.Artwork
import com.example.artfinder.network.ArtApi
import com.example.artfinder.ui.adapter.ArtworkAdapter
import kotlinx.coroutines.launch

class ResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        val query = intent.getStringExtra("query").orEmpty()
        findViewById<TextView>(R.id.resultsTitle).text = "Results for: $query"
        val progress = findViewById<ProgressBar>(R.id.progress)
        val recycler = findViewById<RecyclerView>(R.id.artRecycler)
        recycler.layoutManager = GridLayoutManager(this, 2)
        lifecycleScope.launch {
            progress.visibility = View.VISIBLE
            try {
                val artworks = ArtApi.service.search(query).data.filter {
                    !it.image_id.isNullOrBlank()
                }
                recycler.adapter = ArtworkAdapter(artworks) { item ->
                    startActivity(Intent(this@ResultsActivity, DetailsActivity::class.java).apply {
                        putExtra("id", item.id)
                        putExtra("title", item.title.orEmpty())
                        putExtra("artist", item.artist_display.orEmpty())
                        putExtra("date", item.date_display.orEmpty())
                        putExtra("imageId", item.image_id.orEmpty())
                    })
                }
                if (artworks.isEmpty()) Toast.makeText(this@ResultsActivity,
                    "No artworks with images found", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this@ResultsActivity, "Search failed. Check internet.", Toast.LENGTH_LONG).show()
            } finally { progress.visibility = View.GONE }
        }
    }
}
