package com.example.mapi.api


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.mapi.model.MarvelResponse

interface MarvelApiService {
    @GET("v1/public/characters")
    fun getCharacters(
        @Query("ts") ts: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("limit") limit: Int = 30,
        @Query("offset") offset: Int = 0
    ): Call<MarvelResponse>
}
