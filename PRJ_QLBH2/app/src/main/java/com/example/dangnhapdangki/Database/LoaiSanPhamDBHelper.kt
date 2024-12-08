package com.example.dangnhapdangki.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.dangnhapdangki.Database.DonViDBHelper.Companion
import com.example.dangnhapdangki.Database.DonViDBHelper.Companion.COLUMN_ID_DONVI
import com.example.dangnhapdangki.Database.DonViDBHelper.Companion.TABLE_DONVI
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham

class LoaiSanPhamDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bachhoa.db"
        private const val DATABASE_VERSION = 5
        const val TABLE_LOAI_SANPHAM = "LoaiSanPham"
        const val COLUMN_ID_LOAI_SANPHAM = "idLoai_sp"
        const val COLUMN_NAME = "tenLoai_sp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS $TABLE_LOAI_SANPHAM (
                $COLUMN_ID_LOAI_SANPHAM INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL UNIQUE
            )
        """.trimIndent()

        try {
            db?.execSQL(createTableQuery)
            addSampleData(db) // Thêm dữ liệu mẫu ngay khi tạo bảng
            Log.d("LoaiSanPham", "Table LoaiSanPham created successfully")
        } catch (e: Exception) {
            Log.e("DonViDBHelper", "Error creating table", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LOAI_SANPHAM")
        onCreate(db)
    }

    private fun addSampleData(db: SQLiteDatabase?) {
        val sampleData = listOf(
            "May tinh",
            "Dien thoai",
            "Tu llanh",
        )

        sampleData.forEach { name ->
            val values = ContentValues().apply {
                put(COLUMN_NAME, name)
            }
            db?.insert(TABLE_LOAI_SANPHAM, null, values)
        }
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
}
