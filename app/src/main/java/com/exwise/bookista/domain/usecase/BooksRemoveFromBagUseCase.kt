package com.exwise.bookista.domain.usecase

import com.exwise.bookista.data.Book
import com.exwise.bookista.domain.repository.FirebaseIDSRepository
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore

interface BooksRemoveFromBagUseCase {
    fun removeFromBag(
        book: Book,
        successListener: OnSuccessListener<Any>,
        failureListener: OnFailureListener
    )
}

class BooksRemoveToBagImpl(
    private val firebaseIDSRepository: FirebaseIDSRepository,
    private val firebaseInstance: FirebaseFirestore
) : BooksRemoveFromBagUseCase {
    override fun removeFromBag(
        book: Book,
        successListener: OnSuccessListener<Any>,
        failureListener: OnFailureListener
    ) {
        val data = hashMapOf<String, Any>(
            firebaseIDSRepository.getInABag() to false
        )
        firebaseInstance.collection(firebaseIDSRepository.getCollectionBooks())
            .document(book.id)
            .update(data)
            .addOnSuccessListener { successListener.onSuccess(true) }
            .addOnFailureListener { failureListener.onFailure(it) }
    }
}