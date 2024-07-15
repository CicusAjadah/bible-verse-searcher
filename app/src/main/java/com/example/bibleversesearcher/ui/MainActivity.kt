package com.example.bibleversesearcher.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bibleversesearcher.apiresponse.VerseResponse
import com.example.bibleversesearcher.apiresponse.VersesItem
import com.example.bibleversesearcher.databinding.ActivityMainBinding
import com.example.bibleversesearcher.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by CicusAjadah 12/7/2024
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getRandomQuote()

        binding.btnOther.setOnClickListener {
            showLoading(true)
            getRandomQuote()
        }

        binding.btnSearchSpecific.setOnClickListener {
            val searchSpecificIntent = Intent(this, SpecificVerseActivity::class.java)
            startActivity(searchSpecificIntent)
        }
    }

    private fun getRandomQuote() {
        val call = ApiConfig.getApiService().getRandomBibleVerse()

        call.enqueue(object : Callback<VerseResponse> {
            override fun onResponse(call: Call<VerseResponse>, response: Response<VerseResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val bibleResponse = response.body()
                    bibleResponse?.let {
                        val randomVerse = it.verses.random()
                        displayVerse(randomVerse)
                    }
                } else {
                    showLoading(true)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<VerseResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun displayVerse(verse: VersesItem) {
        binding.tvQuote.text = verse.text
        binding.tvVerse.text = verse.bookName + " " + verse.chapter + ":" + verse.verse
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}