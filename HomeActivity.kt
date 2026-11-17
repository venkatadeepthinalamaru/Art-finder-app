package com.example.artfinder.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.artfinder.R
import com.example.artfinder.ml.ImageLabeler
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var labelsBox: LinearLayout
    private lateinit var labeler: ImageLabeler

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            try {
                val bitmap = if (android.os.Build.VERSION.SDK_INT >= 28) {
                    val source = android.graphics.ImageDecoder.createSource(contentResolver, uri)
                    android.graphics.ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.isMutableRequired = true
                    }
                } else {
                    @Suppress("DEPRECATION")
                    android.provider.MediaStore.Images.Media.getBitmap(contentResolver, uri)
                }
                processBitmap(bitmap)
            } catch (_: Exception) {
                Toast.makeText(this, "Could not open image", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) processBitmap(bitmap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        labeler = ImageLabeler(this)
        labelsBox = findViewById(R.id.labelsBox)

        findViewById<Button>(R.id.cameraButton).setOnClickListener { takePhoto.launch(null) }
        findViewById<Button>(R.id.galleryButton).setOnClickListener { pickImage.launch("image/*") }
        findViewById<Button>(R.id.searchButton).setOnClickListener {
            val q = findViewById<EditText>(R.id.searchInput).text.toString().trim()
            if (q.isNotEmpty()) openResults(q)
            else Toast.makeText(this, "Enter an object or artwork", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.favouritesButton).setOnClickListener {
            startActivity(Intent(this, FavouritesActivity::class.java))
        }
        findViewById<Button>(R.id.signOutButton).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java)); finish()
        }
    }

    private fun processBitmap(bitmap: Bitmap) {
        labelsBox.removeAllViews()
        labeler.label(bitmap) { labels ->
            runOnUiThread {
                if (labels.isEmpty()) {
                    Toast.makeText(this, "No labels found. Try another photo.", Toast.LENGTH_LONG).show()
                } else labels.forEach { label ->
                    val button = Button(this).apply { text = label }
                    button.setOnClickListener { openResults(label) }
                    labelsBox.addView(button)
                }
            }
        }
    }

    private fun openResults(query: String) {
        startActivity(Intent(this, ResultsActivity::class.java).putExtra("query", query))
    }
}
