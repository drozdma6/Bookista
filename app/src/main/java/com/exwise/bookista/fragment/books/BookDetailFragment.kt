package com.exwise.bookista.fragment.books

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.exwise.bookista.GlideApp
import com.exwise.bookista.R
import com.exwise.bookista.data.Book
import com.exwise.bookista.databinding.FragmentBookDetailBinding
import com.exwise.bookista.domain.usecase.BookSwitchInWishlistBooleanUseCase
import com.exwise.bookista.domain.usecase.BooksAddToBagUseCase
import com.exwise.bookista.ext.showAsSnackBar
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class BookDetailFragment : Fragment(R.layout.fragment_book_detail) {

    private lateinit var binding: FragmentBookDetailBinding

    private val booksSwitchInWishlistBooleanUseCase: BookSwitchInWishlistBooleanUseCase by inject { parametersOf() }
    private val booksAddToBagUseCase: BooksAddToBagUseCase by inject { parametersOf() }

    val navArgs by navArgs<BookDetailFragmentArgs>()

    lateinit var book: Book

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookDetailBinding.bind(view)
        book = navArgs.book
        showBook(view, book)

        val backButton: ImageButton = binding.backBtn
        backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        val addToWishList: ImageButton = binding.likeBtn
        addToWishList.setOnClickListener {
            booksSwitchInWishlistBooleanUseCase.switchWishlist(book,
                { inWishlist ->
                    if (inWishlist) {
                        binding.likeBtn.setImageResource(R.drawable.ic_like_selected)
                    } else {
                        binding.likeBtn.setImageResource(R.drawable.ic_like_unselected)
                    }
                    book = book.copy(inWishlist = inWishlist)
                }, {
                    it.printStackTrace()
                    R.string.unknown_error.showAsSnackBar(view)
                })
        }

        binding.addToBag.setOnClickListener {
            if (book.inABag == true) {
                R.string.book_already_in_bag.showAsSnackBar(view)
            } else {
                booksAddToBagUseCase.addToBag(book,
                    {
                        R.string.book_added_to_bag.showAsSnackBar(view)
                    },
                    {
                        it.printStackTrace()
                        R.string.unknown_error.showAsSnackBar(view)
                    })
            }
        }
    }

    private fun showBook(view: View, book: Book) {
        binding.bookDetail.text = book.detail
        binding.txtName.text = book.name
        binding.txtAuthor.text = book.author

        if (book.inWishlist == true) {
            binding.likeBtn.setImageResource(R.drawable.ic_like_selected)
        } else {
            binding.likeBtn.setImageResource(R.drawable.ic_like_unselected)
        }

        GlideApp.with(view)
            .load(
                FirebaseStorage.getInstance().getReferenceFromUrl(book.imageURI.toString())
            )
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.let {
                        //dynamic background image detail
                        val bmp: Bitmap = (it as BitmapDrawable).bitmap
                        val builder = Palette.Builder(bmp)
                        builder.generate { palette: Palette? ->
                            if (palette?.dominantSwatch != null) {
                                val color = palette.dominantSwatch!!.rgb
                                binding.bookDetailBackground.setBackgroundColor(color)
                                binding.addToBag.setBackgroundColor(color)

                                //Status bar color
                                activity?.window?.statusBarColor = color
                            }
                        }
                    }
                    return false
                }
            })
            .placeholder(R.color.ButtonEnabled)
            .into(binding.image)
    }

    override fun onStop() {
        super.onStop()

        //Return status bar color to previous color
        activity?.window?.statusBarColor = resources.getColor(R.color.splashScreenLavander)
    }
}
