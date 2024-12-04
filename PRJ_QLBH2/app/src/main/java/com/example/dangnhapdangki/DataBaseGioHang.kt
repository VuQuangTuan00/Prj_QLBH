package com.example.dangnhapdangki

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor

class DataBaseGioHang(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "GioHangDB2"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "GioHang"
        const val COLUMN_ID = "id"
        const val COLUMN_MASP = "maSP"
        const val COLUMN_TENSP = "tenSP"
        const val COLUMN_LOAISP = "loaiSP"
        const val COLUMN_GIA = "gia"
        const val COLUMN_SOLUONG = "soLuong"
        const val COLUMN_HINH = "hinh"
        const val COLUMN_DACHON = "daChon"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_MASP TEXT,
                $COLUMN_TENSP TEXT,
                $COLUMN_LOAISP TEXT,
                $COLUMN_GIA REAL,
                $COLUMN_SOLUONG INTEGER,
                $COLUMN_HINH TEXT,
                $COLUMN_DACHON INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertGioHang(gioHang: GioHang): Long {
        val db = writableDatabase

        // Kiểm tra sản phẩm đã tồn tại chưa
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_SOLUONG),
            "$COLUMN_MASP = ?",
            arrayOf(gioHang.maSP),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            // Nếu sản phẩm đã tồn tại, cập nhật số lượng
            val currentSoLuong = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SOLUONG))
            val newSoLuong = currentSoLuong + gioHang.soLuong

            // Cập nhật lại số lượng của sản phẩm
            val values = ContentValues().apply {
                put(COLUMN_SOLUONG, newSoLuong)
            }

            // Cập nhật vào cơ sở dữ liệu
            db.update(TABLE_NAME, values, "$COLUMN_MASP = ?", arrayOf(gioHang.maSP)).toLong()
        } else {
            // Nếu sản phẩm chưa tồn tại, chèn mới
            val values = ContentValues().apply {
                put(COLUMN_MASP, gioHang.maSP)
                put(COLUMN_TENSP, gioHang.tenSP)
                put(COLUMN_LOAISP, gioHang.loaiSP)
                put(COLUMN_GIA, gioHang.gia)
                put(COLUMN_SOLUONG, gioHang.soLuong)
                put(COLUMN_HINH, gioHang.hinh)
                put(COLUMN_DACHON, gioHang.daChon)
            }
            db.insert(TABLE_NAME, null, values)
        }.also {
            cursor.close() // Đóng cursor để tránh rò rỉ bộ nhớ
        }
    }


    fun updateGioHang(maSP: String, gioHang: GioHang): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TENSP, gioHang.tenSP)
            put(COLUMN_LOAISP, gioHang.loaiSP)
            put(COLUMN_GIA, gioHang.gia)
            put(COLUMN_SOLUONG, gioHang.soLuong)
            put(COLUMN_HINH, gioHang.hinh)
            put(COLUMN_DACHON, gioHang.daChon)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_MASP = ?", arrayOf(maSP))
    }

    fun deleteGioHang(maSP: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_MASP = ?", arrayOf(maSP))
    }

    fun getAllGioHang(): List<GioHang> {
        val gioHangList = mutableListOf<GioHang>()
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        cursor.use {
            while (it.moveToNext()) {
                val gioHang = GioHang(
                    it.getString(it.getColumnIndexOrThrow(COLUMN_MASP)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_TENSP)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_LOAISP)),
                    it.getDouble(it.getColumnIndexOrThrow(COLUMN_GIA)),
                    it.getInt(it.getColumnIndexOrThrow(COLUMN_SOLUONG)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_HINH)),
                    it.getInt(it.getColumnIndexOrThrow(COLUMN_DACHON))
                )
                gioHangList.add(gioHang)
            }
        }

        return gioHangList
    }

    fun deleteAllGioHang(): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, null, null)  // Xóa tất cả các bản ghi trong bảng
    }


    // Cập nhật số lượng sản phẩm trong giỏ hàng
    fun updateSoLuong(maSP: String, soLuong: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SOLUONG, soLuong)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_MASP = ?", arrayOf(maSP))
    }

    // Cập nhật trạng thái đã chọn của sản phẩm trong giỏ hàng
    fun updateDaChon(maSP: String, daChon: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DACHON, daChon)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_MASP = ?", arrayOf(maSP))
    }

    fun updateAllDaChonToZero(): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DACHON, 0) // Đặt trạng thái "đã chọn" thành 0
        }
        return db.update(TABLE_NAME, values, null, null) // Cập nhật tất cả các sản phẩm trong bảng
    }

    fun deleteGioHangDaChon(): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_DACHON = ?", arrayOf("1"))
    }

    fun isAnyProductSelected(): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_DACHON),
            "$COLUMN_DACHON = ?",
            arrayOf("1"),
            null, null, null
        )

        val isSelected = cursor.count > 0  // Nếu có ít nhất một sản phẩm có daChon = 1
        cursor.close()  // Đừng quên đóng cursor để tránh rò rỉ bộ nhớ
        return isSelected
    }

    fun getTotalPriceOfSelectedProducts(): Double {
        val db = readableDatabase
        var totalPrice = 0.0

        // Truy vấn các sản phẩm đã chọn (daChon = 1)
        val cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_GIA, COLUMN_SOLUONG),  // Chọn cột giá và số lượng
            "$COLUMN_DACHON = ?",                   // Điều kiện: daChon = 1
            arrayOf("1"),
            null, null, null
        )

        cursor.use {
            while (it.moveToNext()) {
                val price = it.getDouble(it.getColumnIndexOrThrow(COLUMN_GIA))
                val quantity = it.getInt(it.getColumnIndexOrThrow(COLUMN_SOLUONG))

                // Tính tổng tiền cho mỗi sản phẩm đã chọn
                totalPrice += price * quantity
            }
        }

        cursor.close()  // Đảm bảo đóng cursor để tránh rò rỉ bộ nhớ
        return totalPrice
    }




}
