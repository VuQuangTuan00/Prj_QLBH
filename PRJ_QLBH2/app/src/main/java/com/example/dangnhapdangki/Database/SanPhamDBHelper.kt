package com.example.dangnhapdangki.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.dangnhapdangki.Database.LoaiSanPhamDBHelper.Companion
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R

class SanPhamDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bachhoa.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_SANPHAM = "SanPham"
        const val COLUMN_ID = "id"
        const val COLUMN_IMG_SP = "hinh_anh"
        const val COLUMN_NAME = "ten"
        const val COLUMN_PRICE = "gia"
        const val COLUMN_QUANTITY = "soLuong"
        const val COLUMN_IDLOAI_SP = "idLoai_sp"
        const val COLUMN_IDDONVI = "idDonVi_sp"
        const val COLUMN_THONGTIN = "thongTin_sp"

        // Bảng LoaiSanPham
        const val TABLE_LOAI_SANPHAM = "LoaiSanPham"
        const val COLUMN_ID_LOAI_SANPHAM = "idLoai_sp"
        const val COLUMN_NAME_LOAI_SP = "tenLoai_sp"

        // Bảng DonVi
        const val TABLE_DONVI = "DonVi"
        const val COLUMN_ID_DONVI = "idDonVi_sp"
        const val COLUMN_NAME_DONVI = "tenDonVi_sp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("ThanhCong1", "1")
        // Tạo bảng SanPham
        val createSanPhamTableQuery = """
           CREATE TABLE $TABLE_SANPHAM (
               $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
               $COLUMN_IMG_SP TEXT NOT NULL,
               $COLUMN_NAME TEXT NOT NULL,
               $COLUMN_PRICE REAL NOT NULL,
               $COLUMN_QUANTITY INTEGER NOT NULL,
               $COLUMN_IDLOAI_SP INTEGER NOT NULL,
               $COLUMN_IDDONVI INTEGER NOT NULL,
               $COLUMN_THONGTIN TEXT NOT NULL,
               FOREIGN KEY ($COLUMN_IDLOAI_SP) REFERENCES $TABLE_LOAI_SANPHAM($COLUMN_ID_LOAI_SANPHAM),
               FOREIGN KEY ($COLUMN_IDDONVI) REFERENCES $TABLE_DONVI($COLUMN_ID_DONVI)
           )
        """.trimIndent()

        val createLoaiSPTableQuery = """
            CREATE TABLE IF NOT EXISTS $TABLE_LOAI_SANPHAM (
                $COLUMN_ID_LOAI_SANPHAM INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME_LOAI_SP TEXT NOT NULL UNIQUE
            )
        """.trimIndent()

        val createDonViTableQuery = """
            CREATE TABLE IF NOT EXISTS $TABLE_DONVI (
                $COLUMN_ID_DONVI INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME_DONVI TEXT NOT NULL UNIQUE
            )
        """.trimIndent()

        try {
            db?.execSQL(createSanPhamTableQuery)
            db?.execSQL(createLoaiSPTableQuery)
            db?.execSQL(createDonViTableQuery)
            addSampleData(db)
            Log.d("SanPhamDBHelper", "Database created successfully")
        } catch (e: Exception) {
            Log.e("SanPhamDBHelper", "Error creating database", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SANPHAM")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LOAI_SANPHAM")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_DONVI")
        onCreate(db)
    }
    fun insertSampleProducts() {
        val db = writableDatabase
        val productData = listOf(
            ContentValues().apply {
                put(COLUMN_IMG_SP, "ic_oil.png") // Thay bằng ID ảnh trong drawable
                put(COLUMN_NAME, "Gạo")
                put(COLUMN_PRICE, 20000.0)
                put(COLUMN_QUANTITY, 10)
                put(COLUMN_IDLOAI_SP, 1) // Thực phẩm
                put(COLUMN_IDDONVI, 1)   // Kg
                put(COLUMN_THONGTIN, "Gạo thơm, chất lượng cao.")
            },
            ContentValues().apply {
                put(COLUMN_IMG_SP, "img_3.png") // Thay bằng ID ảnh trong drawable
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

    private fun addSampleData(db: SQLiteDatabase?) {
        val sampleDataDonVi = listOf(
            "Cái",
            "Chiếc",
            "Hộp",
            "Kg",
            "Lít"
        )

        sampleDataDonVi.forEach { name ->
            val values = ContentValues().apply {
                put(COLUMN_NAME_DONVI, name)
            }
            val result = db?.insert(TABLE_DONVI, null, values)
            if (result != -1L) {
                Log.d("SanPhamDBHelper", "Added new DonVi: $name with ID: $result")
            } else {
                Log.e("SanPhamDBHelper", "Error inserting DonVi: $name")
            }
        }

        val sampleDataLoaiSanPham = listOf(
            "Điện thoại",
            "Laptop",
            "Tủ lạnh"
        )

        sampleDataLoaiSanPham.forEach { name ->
            val values = ContentValues().apply {
                put(COLUMN_NAME_LOAI_SP, name)
            }
            val result = db?.insert(TABLE_LOAI_SANPHAM, null, values)
            if (result != -1L) {
                Log.d("SanPhamDBHelper", "Added new LoaiSanPham: $name with ID: $result")
            } else {
                Log.e("SanPhamDBHelper", "Error inserting LoaiSanPham: $name")
            }
        }
    }
    fun insertSanPham(sanPham: SanPham): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IMG_SP, sanPham.img_sp)
            put(COLUMN_NAME, sanPham.ten_sp)
            put(COLUMN_QUANTITY, sanPham.soLuong_sp)
            put(COLUMN_PRICE, sanPham.gia_sp)
            put(COLUMN_THONGTIN, sanPham.thongTin)
            put(COLUMN_IDLOAI_SP, sanPham.idLoai_sp.idLoai_sp)
            put(COLUMN_IDDONVI, sanPham.idDonVi_sp.idDonVi_sp)
        }

        val result = db.insert(TABLE_SANPHAM, null, values)
        db.close()
        return result != -1L
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

        if (result != -1L) {
            Log.d("SanPhamDBHelper", "Added new product: $name with ID: $result")
        } else {
            Log.e("SanPhamDBHelper", "Error inserting product: $name")
        }

        return result
    }

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
            TABLE_DONVI, arrayOf(COLUMN_ID_DONVI, COLUMN_NAME_DONVI),
            "$COLUMN_ID_DONVI = ?", arrayOf(id.toString()), null, null, null
        )

        cursor.use {
            if (it.moveToFirst()) {
                val idDonViSp = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID_DONVI))
                val tenDonViSp = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME_DONVI))
                return DonVi(idDonViSp, tenDonViSp)
            }
        }
        db.close()
        return null
    }

    private fun getLoaiSanPhamById(id: Int): LoaiSanPham? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_LOAI_SANPHAM, arrayOf(COLUMN_ID_LOAI_SANPHAM, COLUMN_NAME_LOAI_SP),
            "$COLUMN_ID_LOAI_SANPHAM = ?", arrayOf(id.toString()), null, null, null
        )

        cursor.use {
            if (it.moveToFirst()) {
                val idLoaiSp = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID_LOAI_SANPHAM))
                val tenLoaiSp = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME_LOAI_SP))
                return LoaiSanPham(idLoaiSp, tenLoaiSp)
            }
        }
        db.close()
        return null
    }
    fun deleteProduct(productId: Int): Boolean {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(productId.toString())
        val rowsDeleted = db.delete(TABLE_SANPHAM, whereClause, whereArgs)
        db.close()
        return rowsDeleted > 0
    }
    fun updateProduct(sanPham: SanPham): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IMG_SP, sanPham.img_sp)
            put(COLUMN_NAME, sanPham.ten_sp)
            put(COLUMN_PRICE, sanPham.gia_sp)
            put(COLUMN_QUANTITY, sanPham.soLuong_sp)
            put(COLUMN_THONGTIN, sanPham.thongTin)
            put(COLUMN_IDLOAI_SP, sanPham.idLoai_sp.idLoai_sp)
            put(COLUMN_IDDONVI, sanPham.idDonVi_sp.idDonVi_sp)
        }

        // Cập nhật sản phẩm dựa trên ID
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(sanPham.id_sanPham.toString())

        val rowsUpdated = db.update(TABLE_SANPHAM, values, whereClause, whereArgs)
        db.close()

        return rowsUpdated > 0
    }
    // search
    fun searchProducts(keyword: String): List<SanPham> {
        val db = readableDatabase
        val query = """
        SELECT * FROM $TABLE_SANPHAM 
        WHERE $COLUMN_NAME LIKE ? 
        OR $COLUMN_THONGTIN LIKE ?
    """
        val cursor = db.rawQuery(query, arrayOf("%$keyword%", "%$keyword%"))
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
                }
            }
        }
        db.close()
        return products
    }
}

