package com.rezaharis.githubuserapp2.database.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rezaharis.githubuserapp2.database.DatabaseContract
import com.rezaharis.githubuserapp2.database.DatabaseContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "dbfavorites"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITES = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.UserColumns._ID} PRIMARY KEY," +
                "${DatabaseContract.UserColumns.LOGIN} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.AVATAR_URL} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.NAME} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.COMPANY} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.LOCATION} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.REPO} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.FOLLOWERS} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.FOLLOWING} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}