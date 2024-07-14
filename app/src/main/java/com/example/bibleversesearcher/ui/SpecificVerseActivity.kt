package com.example.bibleversesearcher.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bibleversesearcher.R
import com.example.bibleversesearcher.databinding.ActivitySpecificVerseBinding
import com.example.bibleversesearcher.dataclass.BibleVerse

class SpecificVerseActivity : AppCompatActivity() {

    companion object {
        private val TAG = SpecificVerseActivity::class.java.simpleName
    }
    private lateinit var binding:ActivitySpecificVerseBinding
    private val list = ArrayList<BibleVerse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpecificVerseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list.addAll(getListBibles())
        Toast.makeText(this, list.get(0).bookName, Toast.LENGTH_SHORT).show()
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
}