package com.exwise.bookista.domain.usecase

import com.exwise.bookista.data.Book
import com.exwise.bookista.domain.repository.FirebaseIDSRepository
import com.google.firebase.firestore.DocumentSnapshot

interface BookFromQuerySnapshotUseCase {
    fun getBook(documentSnapshot: DocumentSnapshot): Book
}

class BookFromQuerySnapshotImpl(
    private val firebaseIDSRepository: FirebaseIDSRepository
) : BookFromQuerySnapshotUseCase {
    override fun getBook(documentSnapshot: DocumentSnapshot): Book {
        val id = documentSnapshot.id

        val name = documentSnapshot.getString(firebaseIDSRepository.getBookName())

        val author = documentSnapshot.getString(firebaseIDSRepository.getBookAuthor())

        val detail = documentSnapshot.getString(firebaseIDSRepository.getBookDetail())

        val imageURI = documentSnapshot.getString(firebaseIDSRepository.getBookImageURI())

        val inWishlist = documentSnapshot.getBoolean(firebaseIDSRepository.getInWishlist())

        val inABag = documentSnapshot.getBoolean(firebaseIDSRepository.getInABag())

        return Book(id, name, author, detail, imageURI, inWishlist, inABag)
    }
}

