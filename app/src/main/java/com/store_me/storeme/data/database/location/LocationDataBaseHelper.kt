package com.store_me.storeme.data.database.location

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocationDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "locationData.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {  }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {  }

    fun getLocationCode(first: String?, second: String?, third: String?): Int? {
        val db = this.readableDatabase
        val query = "SELECT code FROM location_table WHERE first = ? AND second = ? AND third = ?"
        val cursor = db.rawQuery(query, arrayOf(first, second, third))

        return if (cursor.moveToFirst()) {
            val code = cursor.getInt(cursor.getColumnIndexOrThrow("code"))
            cursor.close()
            code
        } else {
            cursor.close()
            null
        }
    }

    fun searchLocations(query: String): List<LocationEntity> {
        val db = readableDatabase
        val searchTerms = query.split(" ").map { "%$it%" }

        val queryString = StringBuilder("SELECT * FROM location_table WHERE (second IS NOT NULL AND third IS NOT NULL) AND ")
        val queryArgs = mutableListOf<String>()
        searchTerms.forEach { term ->
            queryString.append("(first LIKE ? OR second LIKE ? OR third LIKE ?) AND ")
            queryArgs.add(term)
            queryArgs.add(term)
            queryArgs.add(term)
        }
        val finalQuery = queryString.removeSuffix("AND ").toString()

        val cursor = db.rawQuery(finalQuery, queryArgs.toTypedArray())
        val results = mutableListOf<LocationEntity>()
        if (cursor.moveToFirst()) {
            do {
                val code = cursor.getInt(cursor.getColumnIndexOrThrow("code"))
                val first = cursor.getString(cursor.getColumnIndexOrThrow("first"))
                val second = cursor.getString(cursor.getColumnIndexOrThrow("second"))
                val third = cursor.getString(cursor.getColumnIndexOrThrow("third"))
                results.add(LocationEntity(code, first, second, third))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return results
    }
}

data class LocationEntity(
    val code: Int,
    val first: String?,
    val second: String?,
    val third: String?
)