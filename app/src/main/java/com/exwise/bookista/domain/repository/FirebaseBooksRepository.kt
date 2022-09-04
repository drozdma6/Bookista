package com.exwise.bookista.domain.repository

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

interface FirebaseBooksRepository {
    fun getBooks(snapShotListener: EventListener<QuerySnapshot>)
}

class FirebaseBooksImpl(
    private val firebaseIDSRepository: FirebaseIDSRepository,
    private val firebaseInstance: FirebaseFirestore
): FirebaseBooksRepository {
    override fun getBooks(snapShotListener: EventListener<QuerySnapshot>) {
        firebaseInstance.collection(firebaseIDSRepository.getCollectionBooks())
            .addSnapshotListener(snapShotListener);
    }

}