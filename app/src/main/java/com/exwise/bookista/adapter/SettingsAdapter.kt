package com.exwise.bookista.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exwise.bookista.R
import com.exwise.bookista.databinding.SettingsItemBinding

class SettingsAdapter(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<Pair<String, String>, SettingsAdapter.SettingsHolder>(SettingsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsHolder {
        val binding = SettingsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SettingsHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: SettingsHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    class SettingsHolder(
        private val binding: SettingsItemBinding,
        private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(settingsText: Pair<String, String>, position: Int) {

            binding.root.setOnClickListener { onItemClickListener.onItemClick(position) }

            when (position) {
                0 -> binding.imageSettingsItem.setImageResource(R.drawable.account)
                1 -> binding.imageSettingsItem.setImageResource(R.drawable.notifications)
                2 -> binding.imageSettingsItem.setImageResource(R.drawable.appearance)
                3 -> binding.imageSettingsItem.setImageResource(R.drawable.privacy)
                4 -> binding.imageSettingsItem.setImageResource(R.drawable.help)
                5 -> binding.imageSettingsItem.setImageResource(R.drawable.about)
                6 -> binding.imageSettingsItem.setImageResource(R.drawable.logout)
            }

            val (title, desc) = settingsText
            binding.textSettingsItem.text = title
            binding.textSettingsItemDescription.text = desc
        }
    }

    class SettingsDiffCallback : DiffUtil.ItemCallback<Pair<String, String>>() {

        override fun areContentsTheSame(
            oldItem: Pair<String, String>,
            newItem: Pair<String, String>
        ): Boolean = oldItem.first == newItem.first && oldItem.second == newItem.second

        override fun areItemsTheSame(
            oldItem: Pair<String, String>,
            newItem: Pair<String, String>
        ): Boolean = oldItem == newItem
    }

    interface OnItemClickListener {
        fun onItemClick(index: Int)
    }
}