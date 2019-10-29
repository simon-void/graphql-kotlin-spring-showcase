package com.graphqljava.tutorial.bookdetails.backend

data class Book(
        val id: String,
        val name: String,
        val pageCount: Int,
        val authorIds: List<String>
)

data class Author(
        val id: String,
        val firstName: String,
        val middleNames: String?,
        val lastName: String,
        val bookIds: List<String>
) {
    val fullName: String
        get() = "$firstName ${middleNames?.let { "$it " } ?: ""}$lastName"
}