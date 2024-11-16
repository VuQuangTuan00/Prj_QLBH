package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prj_qlbh.R

class DanhSachSanPham : AppCompatActivity() {
    private lateinit var chiTietSP:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_danh_sach_san_pham)
        setControl()
        setEvent()
    }
    private fun setControl(){
        chiTietSP = findViewById(R.id.chiTietSP)
    }
    private fun setEvent(){
        chiTietSP.setOnClickListener {
            val intent = Intent(this, ChiTietSanPham::class.java)
            startActivity(intent)
        }
    }
}