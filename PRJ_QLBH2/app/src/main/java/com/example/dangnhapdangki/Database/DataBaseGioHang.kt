package com.example.dangnhapdangki.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor
import com.example.dangnhapdangki.Model.GioHang

class DataBaseGioHang(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

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
        const val COLUMN_MAKH = "maKH"
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
                $COLUMN_DACHON INTEGER,
                $COLUMN_MAKH TEXT
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
            "$COLUMN_MASP = ? AND $COLUMN_MAKH = ?",
            arrayOf(gioHang.maSP, gioHang.maKH),
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
            db.update(
                TABLE_NAME,
                values,
                "$COLUMN_MASP = ? AND $COLUMN_MAKH = ?",
                arrayOf(gioHang.maSP, gioHang.maKH)
            ).toLong()
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
                put(COLUMN_MAKH, gioHang.maKH)
            }
            db.insert(TABLE_NAME, null, values)
        }.also {
            cursor.close() // Đóng cursor để tránh rò rỉ bộ nhớ
        }
    }

    fun updateGioHang(maSP: String, maKH: String, gioHang: GioHang): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TENSP, gioHang.tenSP)
            put(COLUMN_LOAISP, gioHang.loaiSP)
            put(COLUMN_GIA, gioHang.gia)
            //put(COLUMN_SOLUONG, gioHang.soLuong)
            put(COLUMN_HINH, gioHang.hinh)
            //put(COLUMN_DACHON, gioHang.daChon)
        }
        return db.update(
            TABLE_NAME,
            values,
            "$COLUMN_MASP = ? AND $COLUMN_MAKH = ?",
            arrayOf(maSP, maKH)
        )
    }

    fun deleteGioHang(maSP: String, maKH: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_MASP = ? AND $COLUMN_MAKH = ?", arrayOf(maSP, maKH))
    }

    fun getAllGioHang(maKH: String): List<GioHang> {
        val gioHangList = mutableListOf<GioHang>()
        val db = readableDatabase
        val cursor: Cursor =
            db.query(TABLE_NAME, null, "$COLUMN_MAKH = ?", arrayOf(maKH), null, null, null)

        cursor.use {
            while (it.moveToNext()) {
                val gioHang = GioHang(
                    it.getString(it.getColumnIndexOrThrow(COLUMN_MASP)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_TENSP)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_LOAISP)),
                    it.getDouble(it.getColumnIndexOrThrow(COLUMN_GIA)),
                    it.getInt(it.getColumnIndexOrThrow(COLUMN_SOLUONG)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_HINH)),
                    it.getInt(it.getColumnIndexOrThrow(COLUMN_DACHON)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_MAKH))
                )
                gioHangList.add(gioHang)
            }
        }

        return gioHangList
    }

    fun deleteAllGioHang(maKH: String): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_NAME,
            "$COLUMN_MAKH = ?",
            arrayOf(maKH)
        )  // Xóa tất cả các bản ghi của khách hàng
    }

    fun updateSoLuong(maSP: String, maKH: String, soLuong: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SOLUONG, soLuong)
        }
        return db.update(
            TABLE_NAME,
            values,
            "$COLUMN_MASP = ? AND $COLUMN_MAKH = ?",
            arrayOf(maSP, maKH)
        )
    }

    fun updateDaChon(maSP: String, maKH: String, daChon: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DACHON, daChon)
        }
        return db.update(
            TABLE_NAME,
            values,
            "$COLUMN_MASP = ? AND $COLUMN_MAKH = ?",
            arrayOf(maSP, maKH)
        )
    }

    fun updateAllDaChonToZero(maKH: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DACHON, 0) // Đặt trạng thái "đã chọn" thành 0
        }
        return db.update(
            TABLE_NAME,
            values,
            "$COLUMN_MAKH = ?",
            arrayOf(maKH)
        ) // Cập nhật tất cả các sản phẩm của khách hàng
    }

    fun deleteGioHangDaChon(maKH: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_DACHON = ? AND $COLUMN_MAKH = ?", arrayOf("1", maKH))
    }

    fun isAnyProductSelected(maKH: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_DACHON),
            "$COLUMN_DACHON = ? AND $COLUMN_MAKH = ?",
            arrayOf("1", maKH),
            null, null, null
        )

        val isSelected = cursor.count > 0  // Nếu có ít nhất một sản phẩm có daChon = 1
        cursor.close()  // Đừng quên đóng cursor để tránh rò rỉ bộ nhớ
        return isSelected
    }

    fun getTotalPriceOfSelectedProducts(maKH: String): Double {
        val db = readableDatabase
        var totalPrice = 0.0

        // Truy vấn các sản phẩm đã chọn (daChon = 1)
        val cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_GIA, COLUMN_SOLUONG),  // Chọn cột giá và số lượng
            "$COLUMN_DACHON = ? AND $COLUMN_MAKH = ?",                   // Điều kiện: daChon = 1 và maKH
            arrayOf("1", maKH),
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

    fun getSelectedGioHang(maKH: String): List<GioHang> {
        val selectedGioHangList = mutableListOf<GioHang>()
        val db = readableDatabase

        // Truy vấn những sản phẩm đã chọn (daChon = 1)
        val cursor: Cursor = db.query(
            TABLE_NAME,
            null,  // Lấy tất cả các cột
            "$COLUMN_DACHON = ? AND $COLUMN_MAKH = ?",  // Điều kiện: daChon = 1 và maKH
            arrayOf("1", maKH),
            null, null, null
        )

        cursor.use {
            while (it.moveToNext()) {
                val gioHang = GioHang(
                    it.getString(it.getColumnIndexOrThrow(COLUMN_MASP)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_TENSP)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_LOAISP)),
                    it.getDouble(it.getColumnIndexOrThrow(COLUMN_GIA)),
                    it.getInt(it.getColumnIndexOrThrow(COLUMN_SOLUONG)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_HINH)),
                    it.getInt(it.getColumnIndexOrThrow(COLUMN_DACHON)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_MAKH))
                )
                selectedGioHangList.add(gioHang)
            }
        }
        cursor.close()  // Đảm bảo đóng cursor để tránh rò rỉ bộ nhớ
        return selectedGioHangList
    }

}
