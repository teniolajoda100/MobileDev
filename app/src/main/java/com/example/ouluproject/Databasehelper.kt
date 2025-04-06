package com.example.ouluproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "users.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
        CREATE TABLE users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            email TEXT UNIQUE,
            password TEXT,
            premium INTEGER DEFAULT 0
        )
    """
        db.execSQL(createTable)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun registerUser(name: String, email: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("email", email)
        values.put("password", password)

        return try {
            db.insertOrThrow("users", null, values)
            db.close()
            true
        } catch (e: Exception) {
            db.close()
            false
        }
    }

    fun validateUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM users WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))

        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    fun getUserByEmail(email: String): Map<String, Any> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name, premium FROM users WHERE email = ?", arrayOf(email))
        val data = mutableMapOf<String, Any>()
        if (cursor.moveToFirst()) {
            data["name"] = cursor.getString(0)
            data["premium"] = cursor.getInt(1)

        }
        cursor.close()
        db.close()
        return data
    }

    fun updatePremiumStatus(email: String, isPremium: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("premium", isPremium)
        }
        val result = db.update("users", values, "email = ?", arrayOf(email))
        db.close()
        return result > 0
    }
    fun getNameByEmail(email: String): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name FROM users WHERE email = ?", arrayOf(email))
        var name = ""
        if (cursor.moveToFirst()) {
            name = cursor.getString(0)
        }
        cursor.close()
        db.close()
        return name
    }


    //update the email thing?
    fun updateEmail(oldEmail: String, newEmail: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("email", newEmail)
        }
        val result = db.update("users", values, "email = ?", arrayOf(oldEmail))
        db.close()
        return result > 0
    }







}
