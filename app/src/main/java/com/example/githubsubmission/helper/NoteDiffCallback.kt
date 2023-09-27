package com.example.githubsubmission.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.githubsubmission.database.FavoriteUser

class NoteDiffCallback(private val oldFavoriteList: List<FavoriteUser>, private val newFavoriteList: List<FavoriteUser>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavoriteList.size
    override fun getNewListSize(): Int = newFavoriteList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteList[oldItemPosition].username == newFavoriteList[newItemPosition].username
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldFavoriteList[oldItemPosition]
        val newNote = newFavoriteList[newItemPosition]
        return oldNote.avatarUrl == newNote.avatarUrl
    }
}