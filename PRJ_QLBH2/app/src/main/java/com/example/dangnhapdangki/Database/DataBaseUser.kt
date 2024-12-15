package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseUser(context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "user_db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "USERS"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USERS_TABLE = "CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT)"
        db?.execSQL(CREATE_USERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_PASSWORD, password)

        // Thêm tài khoản vào bảng
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result !=-1L;
    }

    // Kiểm tra người dùng và mật khẩu (dùng cho đăng nhập)
    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID, COLUMN_USERNAME, COLUMN_PASSWORD),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password),
            null, null, null
        )

        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }
}