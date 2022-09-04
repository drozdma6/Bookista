package com.exwise.bookista.data

import java.io.Serializable

data class Book(
    val id : String,
    val name : String?,
    val author : String?,
    val detail : String?,
    val imageURI : String?,
    val inWishlist: Boolean?,
    val inABag: Boolean?
) : Serializable