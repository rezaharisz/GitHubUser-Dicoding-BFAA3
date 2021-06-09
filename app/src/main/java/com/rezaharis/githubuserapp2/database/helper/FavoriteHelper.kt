package com.rezaharis.githubuserapp2.database.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rezaharis.githubuserapp2.database.DatabaseContract.UserColumns.Companion.LOGIN
import com.rezaharis.githubuserapp2.database.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.rezaharis.githubuserapp2.database.DatabaseContract.UserColumns.Companion._ID
import java.sql.SQLException

class FavoriteHelper(context: Context) {
    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: FavoriteHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): FavoriteHelper =
                INSTANCE ?: synchronized(this){
                    INSTANCE ?: FavoriteHelper(context)
                }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryById(id: String): Cursor{
        return database.query(DATABASE_TABLE, null, "$_ID = ?", arrayOf(id), null, null, null, null)
    }

    fun queryByLogin(login: String): Cursor{
        return database.query(
                DATABASE_TABLE,
                null,
                "$LOGIN = ?", arrayOf(login),
                null,
                null,
                null,
                null
        )
    }

    fun queryAll(): Cursor{
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$_ID ASC"
        )
    }

    fun insert(values: ContentValues?): Long{
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int{
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int{
        return database.delete(DATABASE_TABLE, "$LOGIN = '$id'", null)
    }
}