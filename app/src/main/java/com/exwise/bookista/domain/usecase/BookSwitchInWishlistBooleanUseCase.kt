package com.exwise.bookista.domain.usecase

import com.exwise.bookista.data.Book
import com.exwise.bookista.domain.repository.FirebaseIDSRepository
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore

interface BookSwitchInWishlistBooleanUseCase {
    fun switchWishlist(
        book: Book,
        successListener: OnSuccessListener<Boolean>,
        failureListener: OnFailureListener
    )
}

class BookSwitchInWishlistBooleanImpl(
    private val firebaseIDSRepository: FirebaseIDSRepository,
    private val firebaseInstance: FirebaseFirestore
) : BookSwitchInWishlistBooleanUseCase {
    override fun switchWishlist(
        book: Book,
        successListener: OnSuccessListener<Boolean>,
        failureListener: OnFailureListener
    ) {
        val wishList = book.inWishlist ?: false
        val data = hashMapOf<String, Any>(
            firebaseIDSRepository.getInWishlist() to !wishList
        )
        firebaseInstance.collection(firebaseIDSRepository.getCollectionBooks())
            .document(book.id)
            .update(data)
            .addOnSuccessListener {
                successListener.onSuccess(!wishList)
            }
            .addOnFailureListener { failureListener.onFailure(it) }
    }
}