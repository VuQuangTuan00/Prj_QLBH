package com.example.dangnhapdangki

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DataBaseDonHang(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DonHangDB"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "DonHang"
        const val COLUMN_ID = "id" // Khóa chính tự tăng
        const val COLUMN_MASP = "maSP"
        const val COLUMN_TENSP = "tenSP"
        const val COLUMN_LOAISP = "loaiSP"
        const val COLUMN_DIACHI = "diaChi"
        const val COLUMN_THOIGIAN = "thoiGian"
        const val COLUMN_TRANGTHAI = "trangThai"
        const val COLUMN_GIA = "gia"
        const val COLUMN_SOLUONG = "soLuong"
        const val COLUMN_HINH = "hinh"
        const val COLUMN_MAKH = "maKH" // Mã khách hàng
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_MASP TEXT,
                $COLUMN_TENSP TEXT,
                $COLUMN_LOAISP TEXT,
                $COLUMN_DIACHI TEXT,
                $COLUMN_THOIGIAN TEXT,
                $COLUMN_TRANGTHAI TEXT,
                $COLUMN_GIA REAL,
                $COLUMN_SOLUONG INTEGER,
                $COLUMN_HINH TEXT,
                $COLUMN_MAKH TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Thêm đơn hàng cho một khách hàng
    fun insertDonHang(donHang: DonHang): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MASP, donHang.maSP)
            put(COLUMN_TENSP, donHang.tenSP)
            put(COLUMN_LOAISP, donHang.loaiSP)
            put(COLUMN_DIACHI, donHang.diaChi)
            put(COLUMN_THOIGIAN, donHang.thoiGian)
            put(COLUMN_TRANGTHAI, donHang.trangThai)
            put(COLUMN_GIA, donHang.gia)
            put(COLUMN_SOLUONG, donHang.soLuong)
            put(COLUMN_HINH, donHang.hinh)
            put(COLUMN_MAKH, donHang.maKH)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    // Lấy tất cả đơn hàng theo mã khách hàng
    fun getDonHangByMaKH(maKH: String): List<DonHang> {
        val donHangList = mutableListOf<DonHang>()
        val db = readableDatabase

        val selection = "$COLUMN_MAKH = ?"
        val selectionArgs = arrayOf(maKH)

        // Cập nhật truy vấn để sắp xếp theo thời gian giảm dần
        val cursor = db.query(
            TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "$COLUMN_THOIGIAN DESC" // Sắp xếp theo thời gian giảm dần
        )

        cursor.use {
            while (it.moveToNext()) {
                val donHang = DonHang(
                    maSP = it.getString(it.getColumnIndexOrThrow(COLUMN_MASP)),
                    tenSP = it.getString(it.getColumnIndexOrThrow(COLUMN_TENSP)),
                    loaiSP = it.getString(it.getColumnIndexOrThrow(COLUMN_LOAISP)),
                    diaChi = it.getString(it.getColumnIndexOrThrow(COLUMN_DIACHI)),
                    thoiGian = it.getString(it.getColumnIndexOrThrow(COLUMN_THOIGIAN)),
                    trangThai = it.getString(it.getColumnIndexOrThrow(COLUMN_TRANGTHAI)),
                    gia = it.getDouble(it.getColumnIndexOrThrow(COLUMN_GIA)),
                    soLuong = it.getInt(it.getColumnIndexOrThrow(COLUMN_SOLUONG)),
                    hinh = it.getString(it.getColumnIndexOrThrow(COLUMN_HINH)),
                    maKH = it.getString(it.getColumnIndexOrThrow(COLUMN_MAKH))
                )
                donHangList.add(donHang)
            }
        }
        return donHangList
    }

    fun deleteDonHangByDateAndMaSPAndMaKH(donHang: DonHang): Int {
        val db = writableDatabase

        // Điều kiện xóa: ngày, mã sản phẩm và mã khách hàng
        val selection = "$COLUMN_THOIGIAN = ? AND $COLUMN_MASP = ? AND $COLUMN_MAKH = ?"
        val selectionArgs = arrayOf(
            donHang.thoiGian, // Ngày đặt hàng
            donHang.maSP,     // Mã sản phẩm
            donHang.maKH ?: "" // Mã khách hàng (nếu null, thay bằng chuỗi rỗng)
        )

        // Thực hiện xóa và trả về số dòng bị ảnh hưởng
        return db.delete(TABLE_NAME, selection, selectionArgs)
    }



    // Xóa tất cả đơn hàng theo mã khách hàng
    fun deleteDonHangByMaKH(maKH: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_MAKH = ?", arrayOf(maKH))
    }

    // Cập nhật đơn hàng theo mã khách hàng
    fun updateDonHangByMaKH(maKH: String, donHang: DonHang): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TENSP, donHang.tenSP)
            put(COLUMN_LOAISP, donHang.loaiSP)
            put(COLUMN_DIACHI, donHang.diaChi)
            put(COLUMN_THOIGIAN, donHang.thoiGian)
            put(COLUMN_TRANGTHAI, donHang.trangThai)
            put(COLUMN_GIA, donHang.gia)
            put(COLUMN_SOLUONG, donHang.soLuong)
            put(COLUMN_HINH, donHang.hinh)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_MAKH = ?", arrayOf(maKH))
    }

    fun insertDonHangList(donHangList: List<DonHang>): List<Long> {
        val db = writableDatabase
        val resultList = mutableListOf<Long>()

        // Lấy ngày hiện tại theo định dạng yyyy-MM-dd
        val currentDate = getCurrentDate()

        // Mở giao dịch để tối ưu hiệu suất khi chèn nhiều bản ghi
        db.beginTransaction()
        try {
            for (donHang in donHangList) {
                val values = ContentValues().apply {
                    put(COLUMN_MASP, donHang.maSP)
                    put(COLUMN_TENSP, donHang.tenSP)
                    put(COLUMN_LOAISP, donHang.loaiSP)
                    put(COLUMN_DIACHI, donHang.diaChi)
                    put(COLUMN_THOIGIAN, donHang.thoiGian)  // Thời gian là ngày hiện tại nếu trống
                    put(COLUMN_TRANGTHAI, donHang.trangThai)
                    put(COLUMN_GIA, donHang.gia)
                    put(COLUMN_SOLUONG, donHang.soLuong)
                    put(COLUMN_HINH, donHang.hinh)
                    put(COLUMN_MAKH, donHang.maKH ?: "")  // Nếu maKH null, gán giá trị mặc định là ""
                }

                // Thêm đơn hàng vào cơ sở dữ liệu
                val result = db.insert(TABLE_NAME, null, values)
                resultList.add(result)
            }

            // Cam kết giao dịch (commit)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Kết thúc giao dịch
            db.endTransaction()
        }

        return resultList // Trả về danh sách kết quả chèn (ID của các đơn hàng đã chèn)
    }

    // Hàm để lấy ngày hiện tại theo định dạng yyyy-MM-dd
    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())  // Định dạng ngày tháng
        return dateFormat.format(calendar.time)  // Trả về ngày hiện tại dưới dạng chuỗi
    }

    // Hàm để kiểm tra xem có đơn hàng nào của khách hàng đã được thêm vào cơ sở dữ liệu
    fun existsDonHangByMaKH(maKH: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_MAKH = ?"
        val selectionArgs = arrayOf(maKH)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val exists = cursor.count > 0  // Kiểm tra xem có ít nhất 1 bản ghi không
        cursor.close()
        return exists
    }

    // Hàm kiểm tra xem khách hàng đã đặt đơn hàng trong một ngày cụ thể
    fun isOrderInsertedByDate(maKH: String, date: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_MAKH = ? AND $COLUMN_THOIGIAN = ?"
        val selectionArgs = arrayOf(maKH, date)

        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
        val result = cursor.count > 0  // Kiểm tra nếu có đơn hàng nào của khách hàng vào ngày đó
        cursor.close()
        return result
    }


    // Xóa tất cả đơn hàng trong cơ sở dữ liệu
    fun deleteAllDonHang(): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, null, null)  // Xóa tất cả bản ghi trong bảng DonHang
    }


}
