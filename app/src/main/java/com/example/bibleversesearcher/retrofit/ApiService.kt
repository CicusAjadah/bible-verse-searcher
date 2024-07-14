package com.example.bibleversesearcher.retrofit

import com.example.bibleversesearcher.apiresponse.VerseResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("?random=verse")
    fun getRandomBibleVerse(): Call<VerseResponse>

    @GET("{specificVerse}")
    fun getSpecificVerse(
        @Path("specificVerse") specificVerse: String
    ): Call<VerseResponse>
}