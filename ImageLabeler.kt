package com.example.artfinder.ml

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class ImageLabeler(private val context: Context) {
    fun label(bitmap: Bitmap, callback: (List<String>) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val client = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        client.process(image)
            .addOnSuccessListener { labels ->
                callback(labels.sortedByDescending { it.confidence }
                    .take(3).map { it.text })
                client.close()
            }
            .addOnFailureListener {
                callback(emptyList())
                client.close()
            }
    }
}
