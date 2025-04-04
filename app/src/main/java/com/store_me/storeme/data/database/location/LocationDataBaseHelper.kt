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

    fun getLocationCode(first: String?, second: String?, third: String?): Long? {
        val db = this.readableDatabase
        val query = "SELECT code FROM location_table WHERE first = ? AND second = ? AND third = ?"
        val cursor = db.rawQuery(query, arrayOf(first, second, third))

        return if (cursor.moveToFirst()) {
            val code = cursor.getLong(cursor.getColumnIndexOrThrow("code"))
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
                val code = cursor.getLong(cursor.getColumnIndexOrThrow("code"))
                val first = cursor.getString(cursor.getColumnIndexOrThrow("first"))
                val second = cursor.getString(cursor.getColumnIndexOrThrow("second"))
                val third = cursor.getString(cursor.getColumnIndexOrThrow("third"))
                results.add(LocationEntity(code, first, second, third))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return results
    }

    fun getStoreLocationCode(sigunguCode: String, legalDong: String): Long {
        val db = readableDatabase

        val query = """
        SELECT * FROM location_table 
        WHERE CAST(code AS TEXT) LIKE ?
        ORDER BY code ASC
        """

        val cursor = db.rawQuery(query, arrayOf("$sigunguCode%"))

        val results = mutableListOf<LocationEntity>()
        if(cursor.moveToFirst()) {
            do {
                val code = cursor.getLong(cursor.getColumnIndexOrThrow("code"))
                val first = cursor.getString(cursor.getColumnIndexOrThrow("first"))
                val second = cursor.getString(cursor.getColumnIndexOrThrow("second"))
                val third = cursor.getString(cursor.getColumnIndexOrThrow("third"))
                results.add(LocationEntity(code, first, second, third))
            } while (cursor.moveToNext())
        }
        cursor.close()

        //동읍면 완전 일치
        val exactMatch = results.find { it.third == legalDong }
        if (exactMatch != null) {
            return exactMatch.code
        }

        //부분 일치
        val partialMatch = results.find { it.third?.contains(legalDong) == true }
        if (partialMatch != null) {
            return partialMatch.code
        }

        //일치 항목 없음
        return "${sigunguCode}00000".toLong()
    }

    fun hasExactMatch(query: String): Boolean {
        val db = readableDatabase

        // query는 예: "서울 강남구 역삼동"
        val searchTerms = query.split(" ")

        // 단어가 3개가 아니면 정확 일치가 불가능하다고 판단
        if (searchTerms.size != 3) return false

        val first = searchTerms[0]
        val second = searchTerms[1]
        val third = searchTerms[2]

        val sql = """
        SELECT 1 FROM location_table 
        WHERE first = ? AND second = ? AND third = ?
        LIMIT 1
        """.trimIndent()

        val cursor = db.rawQuery(sql, arrayOf(first, second, third))
        val matchExists = cursor.moveToFirst()
        cursor.close()

        return matchExists
    }

}

data class LocationEntity(
    val code: Long,
    val first: String?,
    val second: String?,
    val third: String?
)