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
        const val COLUMN_MAKH = "maKH"  // Mã khách hàng
        const val COLUMN_DIEM = "diem"  // Điểm của khách hàng
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_MAKH TEXT,
                $COLUMN_DIEM INTEGER,
                UNIQUE($COLUMN_MAKH) ON CONFLICT REPLACE
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Lấy số điểm của một khách hàng theo maKH
    fun getDiem(maKH: String): Int {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_DIEM),
            "$COLUMN_MAKH = ?",
            arrayOf(maKH),
            null, null, null
        )

        var diem = 0
        if (cursor.moveToFirst()) {
            diem = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIEM))
        }

        cursor.close()
        return diem
    }

    // Cập nhật điểm cho một khách hàng
    fun updateDiem(maKH: String, newDiem: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DIEM, newDiem)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_MAKH = ?", arrayOf(maKH)).toLong()
    }

    // Thêm điểm cho một khách hàng
    fun addDiem(maKH: String, addedDiem: Int): Int {
        var currentDiem = getDiem(maKH)  // Lấy điểm hiện tại của khách hàng

        if (currentDiem == 0) {
            // Nếu khách hàng chưa có điểm trong cơ sở dữ liệu, thêm một bản ghi mới
            val values = ContentValues().apply {
                put(COLUMN_MAKH, maKH)
                put(COLUMN_DIEM, addedDiem)
            }
            val db = writableDatabase
            db.insert(TABLE_NAME, null, values)  // Thêm khách hàng mới vào cơ sở dữ liệu
            currentDiem = addedDiem  // Gán điểm hiện tại bằng điểm mới nạp
        } else {
            // Nếu khách hàng đã có điểm, cộng thêm điểm mới
            val newDiem = currentDiem + addedDiem
            updateDiem(maKH, newDiem)  // Cập nhật điểm mới vào cơ sở dữ liệu
            currentDiem = newDiem  // Cập nhật điểm hiện tại
        }

        return currentDiem  // Trả về điểm sau khi nạp
    }


    // Giảm điểm cho một khách hàng, sử dụng kiểu Double
    fun subtractDiem(maKH: String, subtractedDiem: Double): Double {
        val currentDiem = getDiem(maKH).toDouble()  // Lấy điểm hiện tại của khách hàng, chuyển về Double
        val newDiem = (currentDiem - subtractedDiem).coerceAtLeast(0.0)  // Giảm điểm và đảm bảo không nhỏ hơn 0
        updateDiem(maKH, newDiem.toInt())  // Cập nhật điểm trong cơ sở dữ liệu, chuyển về Int
        return newDiem  // Trả về điểm mới (kiểu Double)
    }


    // Kiểm tra xem điểm thanh toán có lớn hơn điểm trong tài khoản không
    fun canPayWithDiem(maKH: String, totalPrice: Int): Boolean {
        val currentDiem = getDiem(maKH).toDouble()  // Giả sử `getDiem` trả về kiểu Int, cần chuyển thành Double
        return currentDiem >= totalPrice  // Kiểm tra nếu số điểm hiện tại lớn hơn hoặc bằng tổng giá trị thanh toán
    }


}
