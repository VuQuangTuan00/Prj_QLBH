package com.example.dangnhapdangki

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseNapDiem(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "NapDiemDB"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "NapDiem"
        const val COLUMN_ID = "id"
        const val COLUMN_DIEM = "diem"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DIEM INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)

        // Insert điểm ban đầu, ví dụ: 0 điểm
        val values = ContentValues().apply {
            put(COLUMN_DIEM, 0)  // Giá trị điểm ban đầu là 0
        }
        db.insert(TABLE_NAME, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Lấy số điểm hiện có
    fun getDiem(): Int {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_DIEM), null, null, null, null, null)

        var diem = 0
        if (cursor.moveToFirst()) {
            diem = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIEM))
        }

        cursor.close()
        return diem
    }

    // Cập nhật số điểm
    fun updateDiem(newDiem: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DIEM, newDiem)
        }
        return db.update(TABLE_NAME, values, null, null)
    }

    // Thêm điểm
    fun addDiem(addedDiem: Int): Int {
        val currentDiem = getDiem()
        val newDiem = currentDiem + addedDiem
        return updateDiem(newDiem)
    }

    // Giảm điểm
    fun subtractDiem(subtractedDiem: Int): Int {
        val currentDiem = getDiem()
        val newDiem = currentDiem - subtractedDiem
        return updateDiem(newDiem)
    }
}
