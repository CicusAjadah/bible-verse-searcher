package com.example.bibleversesearcher.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by CicusAjadah 12/7/2024
 */
@Parcelize
data class BibleVerse (
    val bookName: String,
    val chapter: Int,
    val verse: Int,
    val type: String
) : Parcelable