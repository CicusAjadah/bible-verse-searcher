package com.example.bibleversesearcher.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bibleversesearcher.R
import com.example.bibleversesearcher.apiresponse.VerseResponse
import com.example.bibleversesearcher.apiresponse.VersesItem
import com.example.bibleversesearcher.databinding.ActivitySpecificVerseBinding
import com.example.bibleversesearcher.dataclass.BibleVerse
import com.example.bibleversesearcher.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by CicusAjadah 12/7/2024
 */
class SpecificVerseActivity : AppCompatActivity() {

    companion object {
        private val TAG = SpecificVerseActivity::class.java.simpleName
    }
    private lateinit var binding:ActivitySpecificVerseBinding
    private val list = ArrayList<BibleVerse>()
    private lateinit var types: List<String>
    private lateinit var bookNames: List<String>
    private lateinit var numberChapters: List<Int>
    private val numberVerse = (1..176).toList()
    private var selectedType: String? = null
    private var selectedBookNames: String? = null
    private var selectedChapter: String? = null
    private var selectedVerse: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpecificVerseBinding.inflate(layoutInflater)
        setContentView(binding.root)



        list.addAll(getListBibles())
        showLoading(false)

        createTypeSpinner()

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedType = types[p2]
                createBookSpinner(selectedType!!)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerBook.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedBookNames = bookNames[p2]
                createChapterSpinner(selectedBookNames!!)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerChapter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedChapter = numberChapters[p2].toString()
                createVerseSpinner()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerVerse.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedVerse = numberVerse[p2].toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.btnSearch.setOnClickListener{
            showLoading(true)
            getVerse("$selectedBookNames$selectedChapter:$selectedVerse")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_back, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when(item.itemId){
        R.id.menuBack -> {
            finish()
            true
        }
        else -> true
    }

    private fun displaySelectedValues() {
        val message = "$selectedBookNames$selectedChapter:$selectedVerse"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getListBibles(): ArrayList<BibleVerse> {
        val bookName = resources.getStringArray(R.array.book_name)
        val chapter = resources.getStringArray(R.array.chapter)
        val bookType = resources.getStringArray(R.array.book_type)

        val listBibles = ArrayList<BibleVerse>()
        for(i in bookName.indices) {
            if (i < 39) {
                val bibleOldTestament = BibleVerse(bookName[i], chapter[i].toInt(), 176, bookType[0])
                listBibles.add(bibleOldTestament)
            }
            else {
                val bibleNewTestament = BibleVerse(bookName[i], chapter[i].toInt(), 176, bookType[1])
                listBibles.add(bibleNewTestament)
            }
        }
        return listBibles
    }

    private fun createTypeSpinner(){
        types = list.map { it.type }.distinct()

        val adapter = ArrayAdapter(this, R.layout.spinner_item_layout, types)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout)

        binding.spinnerType.adapter = adapter
    }

    /**
     * Create Spinner for Book Names
     */
    private fun createBookSpinner(bookType: String) {
        val filteredList = list.filter { it.type == bookType }

        bookNames = filteredList.map { it.bookName }

        val filteredAdapter = ArrayAdapter(this, R.layout.spinner_item_layout, bookNames)
        filteredAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout)

        // Set the adapter to the book spinner
        binding.spinnerBook.adapter = filteredAdapter
    }

    /**
     * Create Spinner for Chapter
     */
    private fun createChapterSpinner(bookName: String) {
        val chapter = list.find { it.bookName == bookName }?.chapter

        if (chapter != null) {
            numberChapters = (1..chapter!!).toList()

            val filteredAdapter = ArrayAdapter(this, R.layout.spinner_item_layout, numberChapters)
            filteredAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout)

            // Set the adapter to the chapter spinner
            binding.spinnerChapter.adapter = filteredAdapter
        } else {
            TODO("Not yet implemented")
        }
    }

    /**
     * Create Spinner for Verse
     */
    private fun createVerseSpinner() {
        val filteredAdapter = ArrayAdapter(this, R.layout.spinner_item_layout, numberVerse)
        filteredAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout)

        // Set the adapter to the verse spinner
        binding.spinnerVerse.adapter = filteredAdapter
    }

    /**
     *
     */
    private fun getVerse(input : String) {
        val call = ApiConfig.getApiService().getSpecificVerse(input)

        call.enqueue(object : Callback<VerseResponse> {
            @SuppressLint("ResourceAsColor")
            override fun onResponse(call: Call<VerseResponse>, response: Response<VerseResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val bibleResponse = response.body()
                    bibleResponse?.let {
                        val specificVerse = it.verses[0]
                        displayVerse(specificVerse)
                    }
                } else {
                    showLoading(false)
                    binding.tvQuote.text = "I think the verse you search are not available."
                    binding.tvVerse.text = "Please re-insert another search"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<VerseResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private fun displayVerse(verse: VersesItem) {
        binding.tvQuote.text = verse.text
        binding.tvVerse.text = "$selectedType of $selectedBookNames $selectedChapter:$selectedVerse"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}