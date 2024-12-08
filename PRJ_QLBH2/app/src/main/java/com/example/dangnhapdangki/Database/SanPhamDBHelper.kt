package com.example.dangnhapdangki.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R

class SanPhamDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bachhoa.db"
        private const val DATABASE_VERSION = 4
        const val TABLE_SANPHAM = "SanPham"
        const val COLUMN_ID = "id"
        const val COLUMN_IMG_SP = "hinh_anh"
        const val COLUMN_NAME = "ten"
        const val COLUMN_PRICE = "gia"
        const val COLUMN_QUANTITY = "soLuong"
        const val COLUMN_IDLOAI_SP = "idLoai_sp"
        const val COLUMN_IDDONVI = "idDonVi_sp"
        const val COLUMN_THONGTIN = "thongTin_sp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
//        // Tạo bảng SanPham
//        val createSanPhamTableQuery = """
//           CREATE TABLE SanPham (
//    id INTEGER PRIMARY KEY AUTOINCREMENT,
//    hinh_anh INTEGER NOT NULL,
//    ten TEXT NOT NULL,
//    gia REAL NOT NULL,
//    soLuong INTEGER NOT NULL,
//    idLoai_sp INTEGER NOT NULL,
//    idDonVi_sp INTEGER NOT NULL,
//    thongTin TEXT NOT NULL,
//    FOREIGN KEY (idLoai_sp) REFERENCES LoaiSanPham(idLoai_sp),
//    FOREIGN KEY (idDonVi_sp) REFERENCES DonVi(idDonVi_sp)
//)
//        """.trimIndent()
//
//
//        try {
//            db?.execSQL(createSanPhamTableQuery)
//            insertSampleProducts()
//            Log.d("SanPhamDBHelper", "Database created successfully")
//        } catch (e: Exception) {
//            Log.e("SanPhamDBHelper", "Error creating database", e)
//        }
    }
    fun insertSampleProducts() {
        val db = writableDatabase
        val productData = listOf(
            ContentValues().apply {
                put(COLUMN_IMG_SP, "asasas") // Thay bằng ID ảnh trong drawable
                put(COLUMN_NAME, "Gạo")
                put(COLUMN_PRICE, 20000.0)
                put(COLUMN_QUANTITY, 10)
                put(COLUMN_IDLOAI_SP, 1) // Thực phẩm
                put(COLUMN_IDDONVI, 1)   // Kg
                put(COLUMN_THONGTIN, "Gạo thơm, chất lượng cao.")
            },
            ContentValues().apply {
                put(COLUMN_IMG_SP, "R.drawable.sample_image2") // Thay bằng ID ảnh trong drawable
                put(COLUMN_NAME, "Nước cam")
                put(COLUMN_PRICE, 15000.0)
                put(COLUMN_QUANTITY, 20)
                put(COLUMN_IDLOAI_SP, 2) // Đồ uống
                put(COLUMN_IDDONVI, 2)   // Lít
                put(COLUMN_THONGTIN, "Nước cam ép tươi nguyên chất.")
            }
        )

        productData.forEach { db.insert(TABLE_SANPHAM, null, it) }

        db.close()
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        // Đảm bảo các bảng được xóa và tạo lại khi nâng cấp cơ sở dữ liệu
//        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SANPHAM")
//        onCreate(db)
    }

    fun addProduct(
        img: Int,
        name: String,
        price: Double,
        quantity: Int,
        loaiSanPhamId: Int,
        donViId: Int,
        thongTin: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IMG_SP, img)
            put(COLUMN_NAME, name)
            put(COLUMN_PRICE, price)
            put(COLUMN_QUANTITY, quantity)
            put(COLUMN_IDLOAI_SP, loaiSanPhamId)
            put(COLUMN_IDDONVI, donViId)
            put(COLUMN_THONGTIN, thongTin)
        }

        val result = db.insert(TABLE_SANPHAM, null, values)
        db.close()
        if (result == -1L) {
            Log.e("SanPhamDBHelper", "Error inserting product: $name")
        }
        return result
    }

    // Lấy tất cả sản phẩm từ cơ sở dữ liệu
    fun getAllProducts(): List<SanPham> {
        val db = readableDatabase
        val cursor = db.query(TABLE_SANPHAM, null, null, null, null, null, null)
        val products = mutableListOf<SanPham>()

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val img = it.getString(it.getColumnIndexOrThrow(COLUMN_IMG_SP))
                val price = it.getDouble(it.getColumnIndexOrThrow(COLUMN_PRICE))
                val soLuong = it.getInt(it.getColumnIndexOrThrow(COLUMN_QUANTITY))
                val loaiSanPham = getLoaiSanPhamById(it.getInt(it.getColumnIndexOrThrow(COLUMN_IDLOAI_SP)))
                val donVi = getDonViById(it.getInt(it.getColumnIndexOrThrow(COLUMN_IDDONVI)))
                val thongTin = it.getString(it.getColumnIndexOrThrow(COLUMN_THONGTIN))

                if (loaiSanPham != null && donVi != null) {
                    products.add(SanPham(id, img, name, loaiSanPham, soLuong, donVi, price, thongTin))
                } else {
                    Log.e("SanPhamDBHelper", "Invalid data for product ID: $id")
                }
            }
        }
        db.close()
        return products
    }

    private fun getDonViById(id: Int): DonVi? {
        val db = readableDatabase
        val cursor = db.query(
            "DonVi", arrayOf("idDonVi_sp", "tenDonVi_sp"),
            "idDonVi_sp = ?", arrayOf(id.toString()), null, null, null
        )

        cursor.use {
            if (it.moveToFirst()) {
                val idDonViSp = it.getInt(it.getColumnIndexOrThrow("idDonVi_sp"))
                val tenDonViSp = it.getString(it.getColumnIndexOrThrow("tenDonVi_sp"))
                return DonVi(idDonViSp, tenDonViSp)
            }
        }
        db.close()
        return null // Trả về null nếu không tìm thấy
    }

    private fun getLoaiSanPhamById(id: Int): LoaiSanPham? {
        val db = readableDatabase
        val cursor = db.query(
            "LoaiSanPham", arrayOf("idLoai_sp", "tenLoai_sp"),
            "idLoai_sp = ?", arrayOf(id.toString()), null, null, null
        )

        cursor.use {
            if (it.moveToFirst()) {
                val idLoaiSp = it.getInt(it.getColumnIndexOrThrow("idLoai_sp"))
                val tenLoaiSp = it.getString(it.getColumnIndexOrThrow("tenLoai_sp"))
                return LoaiSanPham(idLoaiSp, tenLoaiSp)
            }
        }
        db.close()
        return null // Trả về null nếu không tìm thấy
    }
}
