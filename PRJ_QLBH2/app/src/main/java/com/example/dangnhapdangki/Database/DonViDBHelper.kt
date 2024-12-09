package com.example.dangnhapdangki.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.demo_recycleview.Model.DonVi

class DonViDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bachhoa.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_DONVI = "DonVi"
        const val COLUMN_ID_DONVI = "idDonVi_sp"
        const val COLUMN_NAME = "tenDonVi_sp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("Caillll", "Database is being created")
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS $TABLE_DONVI (
                $COLUMN_ID_DONVI INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL UNIQUE
            )
        """.trimIndent()

        try {
            db?.execSQL(createTableQuery)
            Log.d("Don vi", "Table LoaiSanPham created successfully")
        } catch (e: Exception) {
            Log.e("DonViDBHelper", "Error creating table", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_DONVI")
        onCreate(db)
    }


    fun getAllDonVi(): List<DonVi> {
        val db = readableDatabase
        val cursor = db.query(TABLE_DONVI, null, null, null, null, null, null)
        val donViList = mutableListOf<DonVi>()

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID_DONVI))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                donViList.add(DonVi(id, name))
            }
        }
        db.close()
        return donViList
    }

    fun addDonVi(name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
        }
        val result = db.insert(TABLE_DONVI, null, values)
        db.close()
        return result
    }
}
