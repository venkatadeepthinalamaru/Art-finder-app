package com.example.artfinder.model

data class Artwork(
    val id: Int,
    val title: String?,
    val artist_display: String?,
    val date_display: String?,
    val image_id: String?
) {
    val imageUrl: String?
        get() = image_id?.takeIf { it.isNotBlank() }?.let {
            "https://www.artic.edu/iiif/2/$it/full/843,/0/default.jpg"
        }
}
data class ArtSearchResponse(val data: List<Artwork> = emptyList())
