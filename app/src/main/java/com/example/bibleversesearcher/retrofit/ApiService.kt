package com.example.bibleversesearcher.retrofit

import com.example.bibleversesearcher.apiresponse.VerseResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by CicusAjadah 12/7/2024
 */
interface ApiService {
    @GET("/?random=verse")
    fun getRandomBibleVerse(): Call<VerseResponse>

    @GET("/{specificVerse}")
    fun getSpecificVerse(
        @Path("specificVerse") specificVerse: String
    ): Call<VerseResponse>
}