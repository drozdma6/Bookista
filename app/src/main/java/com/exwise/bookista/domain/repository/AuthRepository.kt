package com.exwise.bookista.domain.repository

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.rpc.context.AttributeContext

interface AuthRepository {
    fun login(
        email: String,
        password: String,
        successListener: OnSuccessListener<in AuthResult>,
        failureListener: OnFailureListener
    )

    fun register(
        email: String,
        password: String,
        successListener: OnSuccessListener<in AuthResult>,
        failureListener: OnFailureListener
    )

    fun sendPasswordResetEmail(
        email: String,
        successListener: OnSuccessListener<in Void>,
        failureListener: OnFailureListener
    )

    fun sendVerificationEmail(
        user: FirebaseUser,
        successListener: OnSuccessListener<in Void>,
        failureListener: OnFailureListener
    )

    fun reAuthenticateUser(user: FirebaseUser, email: String, password: String)

    fun logOut()

    fun isUserLoggedIn() : Boolean
}

class AuthRepositoryImpl(private val auth: FirebaseAuth) : AuthRepository {

    override fun login(
        email: String,
        password: String,
        successListener: OnSuccessListener<in AuthResult>,
        failureListener: OnFailureListener
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun register(
        email: String,
        password: String,
        successListener: OnSuccessListener<in AuthResult>,
        failureListener: OnFailureListener
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun sendPasswordResetEmail(
        email: String,
        successListener: OnSuccessListener<in Void>,
        failureListener: OnFailureListener
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun sendVerificationEmail(
        user: FirebaseUser,
        successListener: OnSuccessListener<in Void>,
        failureListener: OnFailureListener
    ) {
        user.sendEmailVerification()
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun reAuthenticateUser(user: FirebaseUser, email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun logOut() {
        auth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        val user = auth.currentUser
        return user != null
    }
}

