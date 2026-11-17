package com.example.artfinder.network

import com.example.artfinder.model.ArtSearchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ArtApi {
    @GET("artworks/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("fields") fields: String =
            "id,title,artist_display,date_display,image_id",
        @Query("limit") limit: Int = 30
    ): ArtSearchResponse

    companion object {
        val service: ArtApi by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.artic.edu/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ArtApi::class.java)
        }
    }
}
