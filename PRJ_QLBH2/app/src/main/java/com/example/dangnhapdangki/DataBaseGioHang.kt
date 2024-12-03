package com.example.dangnhapdangki

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseGioHang(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "GioHangDB"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "GioHang"
        const val COLUMN_ID = "id"
        const val COLUMN_MASP = "maSP"
        const val COLUMN_TENSP = "tenSP"
        const val COLUMN_LOAISP = "loaiSP"
        const val COLUMN_GIA = "gia"
        const val COLUMN_SOLUONG = "soLuong"
        const val COLUMN_HINH = "hinh"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_MASP TEXT, " +
                "$COLUMN_TENSP TEXT, " +
                "$COLUMN_LOAISP TEXT, " +
                "$COLUMN_GIA REAL, " +
                "$COLUMN_SOLUONG INTEGER, " +
                "$COLUMN_HINH TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertGioHang(gioHang: GioHang): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MASP, gioHang.maSP)
            put(COLUMN_TENSP, gioHang.tenSP)
            put(COLUMN_LOAISP, gioHang.loaiSP)
            put(COLUMN_GIA, gioHang.gia)
            put(COLUMN_SOLUONG, gioHang.soLuong)
            put(COLUMN_HINH, gioHang.hinh)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun updateGioHang(maSP: String, gioHang: GioHang): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TENSP, gioHang.tenSP)
            put(COLUMN_LOAISP, gioHang.loaiSP)
            put(COLUMN_GIA, gioHang.gia)
            put(COLUMN_SOLUONG, gioHang.soLuong)
            put(COLUMN_HINH, gioHang.hinh)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_MASP = ?", arrayOf(maSP))
    }

    fun deleteGioHang(maSP: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_MASP = ?", arrayOf(maSP))
    }

    fun getAllGioHang(): List<GioHang> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val gioHangList = mutableListOf<GioHang>()
        with(cursor) {
            while (moveToNext()) {
                val gioHang = GioHang(
                    getString(getColumnIndexOrThrow(COLUMN_MASP)),
                    getString(getColumnIndexOrThrow(COLUMN_TENSP)),
                    getString(getColumnIndexOrThrow(COLUMN_LOAISP)),
                    getDouble(getColumnIndexOrThrow(COLUMN_GIA)),
                    getInt(getColumnIndexOrThrow(COLUMN_SOLUONG)),
                    getString(getColumnIndexOrThrow(COLUMN_HINH)) // Đảm bảo lấy đúng giá trị hình ảnh
                )
                gioHangList.add(gioHang)
            }
        }
        cursor.close()
        return gioHangList
    }
}
