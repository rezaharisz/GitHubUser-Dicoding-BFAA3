package com.rezaharis.githubfavorites.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.rezaharis.githubuserapp2"
    const val SCHEME = "content"

    class UserColumns: BaseColumns{
        companion object{
            private const val TABLE_NAME = "favorites"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val AVATAR_URL = "avatar_url"
            const val NAME = "name"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPO = "repo"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}