package com.exwise.bookista.di

import com.exwise.bookista.domain.repository.*
import com.exwise.bookista.domain.usecase.*
import com.exwise.bookista.viewModel.BagFragmentViewModel
import com.exwise.bookista.viewModel.authentication.LoginFragmentViewModel
import com.exwise.bookista.viewModel.authentication.RegisterFragmentViewModel
import com.exwise.bookista.viewModel.authentication.ResetPasswordViewModel
import com.exwise.bookista.viewModel.home.BooksFragmentViewModel
import com.exwise.bookista.viewModel.home.SettingsFragmentViewModel
import com.exwise.bookista.viewModel.home.WishlistFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        LoginFragmentViewModel(
            auth = FirebaseAuth.getInstance(),
            authRepository = get()
        )
    }
    viewModel {
        RegisterFragmentViewModel(
            auth = FirebaseAuth.getInstance(),
            authRepository = get()
        )
    }
    viewModel {
        ResetPasswordViewModel(
            authRepository = get()
        )
    }

    viewModel {
        BooksFragmentViewModel(
            booksRepository = get(),
            bookFromQuerySnapshotUseCase = get(),
            booksSwitchInWishlistBooleanUseCase = get(),
            booksAddToBagUseCase = get()
        )
    }

    viewModel {
        SettingsFragmentViewModel(
            authRepository = get()
        )
    }

    viewModel {
        WishlistFragmentViewModel(
            booksRepository = get(),
            booksSwitchInWishlistBooleanUseCase = get(),
            bookFromQuerySnapshotUseCase = get()
        )
    }

    viewModel {
        BagFragmentViewModel(
            booksRepository = get(),
            booksRemoveFromBagUseCase = get(),
            bookFromQuerySnapshotUseCase = get()
        )
    }
}

val firebaseModule = module {
    //Repository
    single { AuthRepositoryImpl(auth = FirebaseAuth.getInstance()) as AuthRepository }
    single { FirebaseIDSImpl() as FirebaseIDSRepository }
    single {
        FirebaseBooksImpl(
            firebaseIDSRepository = get(),
            firebaseInstance = FirebaseFirestore.getInstance()
        ) as FirebaseBooksRepository
    }

    //UseCase
    single {
        BookFromQuerySnapshotImpl(
            firebaseIDSRepository = get()
        ) as BookFromQuerySnapshotUseCase
    }
    single {
        BookSwitchInWishlistBooleanImpl(
            firebaseIDSRepository = get(),
            firebaseInstance = FirebaseFirestore.getInstance()
        ) as BookSwitchInWishlistBooleanUseCase
    }
    single {
        BooksAddToBagImpl(
            firebaseIDSRepository = get(),
            firebaseInstance = FirebaseFirestore.getInstance()
        ) as BooksAddToBagUseCase
    }

    single {
        BooksRemoveToBagImpl(
            firebaseIDSRepository = get(),
            firebaseInstance = FirebaseFirestore.getInstance()
        ) as BooksRemoveFromBagUseCase
    }
}