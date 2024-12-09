package com.example.dangnhapdangki.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.demo_recycleview.Model.LoaiSanPham

class LoaiSanPhamDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bachhoa.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_LOAI_SANPHAM = "LoaiSanPham"
        const val COLUMN_ID_LOAI_SANPHAM = "idLoai_sp"
        const val COLUMN_NAME = "tenLoai_sp"
    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun getAllLoaiSanPham(): List<LoaiSanPham> {
        val db = readableDatabase
        val cursor = db.query(TABLE_LOAI_SANPHAM, null, null, null, null, null, null)
        val loaiSanPhamList = mutableListOf<LoaiSanPham>()

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID_LOAI_SANPHAM))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                loaiSanPhamList.add(LoaiSanPham(id, name))
            }
        }
        db.close()
        return loaiSanPhamList
    }

    fun addLoaiSanPham(name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
        }
        val result = db.insert(TABLE_LOAI_SANPHAM, null, values)
        db.close()
        return result
    }
}
