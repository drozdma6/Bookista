# Bookista

Perfect app for borrowing books from your local library!
Work for BI-SP1

## Content

- [Wireframing](#wireframing)
- [Usage](#usage)
- [How it works](#how-it-works)
    
## Wireframing
Link to [Figma](https://www.figma.com/file/8Nlup1a4Cye9l71INS7UMa/bookista?node-id=0%3A1)

## Usage

Create account, confirm verification email and you are ready to go! Add books to your wishlist or add them to your bag (Coming soon). Read book information and find out something new.

## How it works

  - [Authentication](#authentication)
  - [Database](#database)
  - [Images](#images)
    - [Bucket](#bucket)
    - [Glide](#glide)
  - [Dependency Injection](#dependency-injection)
  - [Book list](#book-list)

### Authentication

For authentication we are using Firebase authetication. It provides not only logging in, but sending verification email and also resetting password. Only thing we had to do was enable it in project [console](https://console.firebase.google.com), add authentication dependency. More details in [documentation](https://firebase.google.com/docs/auth/android/firebaseui). Implementation in [Authentication Fragments](https://gitlab.fit.cvut.cz/bilybran/bookista/tree/master/app/src/main/java/com/exwise/bookista/fragment/authentication) that use functions from [AuthRepository](https://gitlab.fit.cvut.cz/bilybran/bookista/blob/master/app/src/main/java/com/exwise/bookista/domain/repository/AuthRepository.kt).

### Database

For database we are using Firestore database. It is real-time No-SQL (document) database. Data is ordered in collections and documents. After enabling it in project [console](https://console.firebase.google.com) and adding firestore dependency, we can listen to changes in the database in real time. More details in [documentation](https://firebase.google.com/docs/firestore). Example of implementation: [FirebaseBooksRepository](https://gitlab.fit.cvut.cz/bilybran/bookista/blob/master/app/src/main/java/com/exwise/bookista/domain/repository/FirebaseBooksRepository.kt) adds new listener for collection 'books' and [BooksFragmentViewModel](https://gitlab.fit.cvut.cz/bilybran/bookista/blob/master/app/src/main/java/com/exwise/bookista/viewModel/home/BooksFragmentViewModel.kt) listenes for changes.

### Images

For storing images we are using storage [bucket](#bucket). For loading images from storage bucket we are using [glide](#glide).

#### Bucket

Firebase allows for file storage. Data can be separated into numerous buckets. We are using one bucket for images of books. Creating bucket is little complicated in comparison to authentication and database. In project [console](https://console.firebase.google.com) we added new bucket and we were forwarded to google cloud, where we followed instruction dialog. After that, bucket is created and we can add our images.

#### Glide

For loading images into the app, we are using [Glide](https://github.com/bumptech/glide). Documentation includes in previous link.

### Dependency Injection

We are, of cource, using dependency injection with [Koin](https://insert-koin.io). First we have to "register" class in [AppModule](https://gitlab.fit.cvut.cz/bilybran/bookista/blob/master/app/src/main/java/com/exwise/bookista/di/AppModule.kt). We have two modules, one for View Models and one for Firebase classes. (Although it's not necessary). Then in [BookistaApplication](https://gitlab.fit.cvut.cz/bilybran/bookista/blob/master/app/src/main/java/com/exwise/bookista/BookistaApplication.kt) we tell Koin which modules should be created. 

### Book list

For showing books, we are using [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview?gclid=Cj0KCQjw3IqSBhCoARIsAMBkTb2o_n4fPwf4MbZQgKM8EFm5WzGU3nHNePuAOG96Vo_lprqEZMifYLEaArEZEALw_wcB&gclsrc=aw.ds). RecyclerView is much more efficient than ListView. If we have 100,000 items that we want to show, ListView creates (inflates) 100,000 item views at once. Recycler view creates only as many item views that fit parent view (fit screen) and only changes data in them.
