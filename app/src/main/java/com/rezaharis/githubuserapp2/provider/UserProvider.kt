package com.rezaharis.githubuserapp2.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.rezaharis.githubuserapp2.database.DatabaseContract.AUTHORITY
import com.rezaharis.githubuserapp2.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.rezaharis.githubuserapp2.database.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.rezaharis.githubuserapp2.database.helper.FavoriteHelper

class UserProvider: ContentProvider() {
    companion object{
        private const val USER = 1
        private const val USER_ID = 2
        private lateinit var favoriteHelper: FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        return when(sUriMatcher.match(uri)){
            USER -> favoriteHelper.queryAll()
            USER_ID -> favoriteHelper.queryByLogin(uri.lastPathSegment.toString())

            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when(USER){
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val delete: Int = when(USER_ID){
            sUriMatcher.match(uri) -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return delete
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val update: Int = when(USER_ID){
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return update
    }
}