package com.example.artfinder.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.artfinder.R
import com.example.artfinder.data.AppDatabase
import com.example.artfinder.data.ArtworkEntity
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {
    private var saved = false
    private var artworkId = 0
    private lateinit var titleText: String
    private lateinit var artistText: String
    private lateinit var dateText: String
    private lateinit var imageId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        artworkId = intent.getIntExtra("id", 0)
        titleText = intent.getStringExtra("title").orEmpty()
        artistText = intent.getStringExtra("artist").orEmpty()
        dateText = intent.getStringExtra("date").orEmpty()
        imageId = intent.getStringExtra("imageId").orEmpty()

        findViewById<TextView>(R.id.detailTitle).text = titleText
        findViewById<TextView>(R.id.detailArtist).text = artistText
        findViewById<TextView>(R.id.detailDate).text = dateText
        val url = "https://www.artic.edu/iiif/2/$imageId/full/843,/0/default.jpg"
        Glide.with(this).load(url).into(findViewById<ImageView>(R.id.detailImage))

        findViewById<Button>(R.id.shareButton).setOnClickListener {
            val send = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "$titleText
$artistText
$dateText
$url")
            }
            startActivity(Intent.createChooser(send, "Share artwork"))
        }
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            lifecycleScope.launch {
                val dao = AppDatabase.get(this@DetailsActivity).artworkDao()
                if (dao.isSaved(artworkId)) {
                    dao.delete(artworkId)
                    runOnUiThread { Toast.makeText(this@DetailsActivity, "Removed from favourites", Toast.LENGTH_SHORT).show() }
                } else {
                    dao.insert(ArtworkEntity(artworkId, titleText, artistText, dateText, imageId))
                    runOnUiThread { Toast.makeText(this@DetailsActivity, "Saved to favourites", Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }
}
