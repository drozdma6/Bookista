package com.exwise.bookista.domain.repository

interface FirebaseIDSRepository {
    fun getCollectionBooks(): String
    fun getBookName(): String
    fun getBookAuthor(): String
    fun getBookDetail(): String
    fun getBookImageURI(): String
    fun getInWishlist(): String
    fun getInABag(): String
}

class FirebaseIDSImpl : FirebaseIDSRepository {
    override fun getCollectionBooks(): String = "books"
    override fun getBookName(): String = "name"
    override fun getBookAuthor(): String = "author"
    override fun getBookDetail(): String = "detail"
    override fun getBookImageURI(): String = "imageURI"
    override fun getInWishlist(): String = "inWishlist"
    override fun getInABag(): String = "inABag"
}