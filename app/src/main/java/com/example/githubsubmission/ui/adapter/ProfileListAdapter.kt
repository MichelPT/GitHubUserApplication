package com.example.githubsubmission.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubsubmission.data.response.ItemsItem
import com.example.githubsubmission.databinding.ProfileItemBinding

class ProfileListAdapter :
    ListAdapter<ItemsItem, ProfileListAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallBack

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallback = onItemClickCallBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: ProfileItemBinding =
            ProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val profile = getItem(position)
        holder.bind(profile)
        holder.itemView.setOnClickListener { this.onItemClickCallback.onItemClicked(profile) }
    }

    class ItemViewHolder(private val binding: ProfileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: ItemsItem) {
            binding.tvProfileTitle.text = "ID ${profile.id}"
            binding.tvProfileDesc.text = profile.login
            Glide.with(binding.root)
                .load(profile.avatarUrl)
                .into(binding.imgProfilePhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(profile: ItemsItem)
    }
}