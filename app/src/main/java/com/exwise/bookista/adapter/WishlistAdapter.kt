package com.exwise.bookista.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exwise.bookista.GlideApp
import com.exwise.bookista.R
import com.exwise.bookista.data.Book
import com.exwise.bookista.databinding.WishlistItemBinding
import com.google.firebase.storage.FirebaseStorage

class WishlistAdapter(private val onRemoveClickLister: OnRemoveClickListener) :
    ListAdapter<Book, WishlistAdapter.WishlistHolder>(
        BooksAdapter.BooksDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistHolder {
        val binding = WishlistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WishlistHolder(binding, onRemoveClickLister)
    }

    override fun onBindViewHolder(holder: WishlistHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    class WishlistHolder(
        private val binding: WishlistItemBinding,
        private val onRemoveClickLister: OnRemoveClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.txtName.text = book.name
            binding.txtAuthor.text = book.author

            binding.removeFromWishlist.setOnClickListener { onRemoveClickLister.onRemoveClick(book) }

            bindImage(book)
        }

        private fun bindImage(book: Book) {
            if (book.imageURI.isNullOrBlank()) {
                binding.imgBook.setImageResource(R.drawable.ic_baseline_cloud_download_24)
            } else {
                GlideApp.with(itemView)
                    .load(
                        FirebaseStorage.getInstance().getReferenceFromUrl(book.imageURI.toString())
                    )
                    .placeholder(R.color.ButtonEnabled)
                    .into(binding.imgBook)
            }
        }
    }

    interface OnRemoveClickListener {
        fun onRemoveClick(book: Book)
    }
}