package com.graphqljava.tutorial.bookdetails.backend

import org.springframework.stereotype.Service

@Service
class DataService {
    fun getBook(id: String): Book? = books[id]
    fun getAuthor(id: String): Author? = authors[id]
}

private val books = listOf(
        Book("book-1",
                "Harry Potter and the Philosopher's Stone",
                223,
                listOf("author-1")),
        Book("book-2",
                "Moby Dick",
                635,
                listOf("author-2")),
        Book("book-3",
                "Book with many Authors",
                371,
                listOf("author-1", "author-2"))
).associateBy(Book::id)//.toMutableMap()

private val authors = listOf(
        Author("author-1",
                "Joanne",
                "K.",
                "Rowling",
                listOf("book-1", "book-3")),
        Author("author-2",
                "Herman",
                null,
                "Melville",
                listOf("book-2", "book-3"))
).associateBy(Author::id)//.toMutableMap()