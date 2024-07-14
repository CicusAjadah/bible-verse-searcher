package com.example.bibleversesearcher.apiresponse

import com.google.gson.annotations.SerializedName

data class VerseResponse(

	@field:SerializedName("reference")
	val reference: String,

	@field:SerializedName("translation_id")
	val translationId: String,

	@field:SerializedName("translation_note")
	val translationNote: String,

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("translation_name")
	val translationName: String,

	@field:SerializedName("verses")
	val verses: List<VersesItem>
)

data class VersesItem(

	@field:SerializedName("chapter")
	val chapter: Int,

	@field:SerializedName("book_id")
	val bookId: String,

	@field:SerializedName("verse")
	val verse: Int,

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("book_name")
	val bookName: String
)
