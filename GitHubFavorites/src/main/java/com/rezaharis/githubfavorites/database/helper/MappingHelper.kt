package com.rezaharis.githubfavorites.database.helper

import android.database.Cursor
import com.rezaharis.githubfavorites.database.DatabaseContract
import com.rezaharis.githubfavorites.entity.User

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<User>{
        val favoriteList = ArrayList<User>()

        favoriteCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
                val repo = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.REPO))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING))
                favoriteList.add(User(id, login, avatarUrl, name, company, location, repo, followers, following))
            }
        }
        return favoriteList
    }
}