package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.prj_qlbh.R

class DanhSachSanPham : AppCompatActivity() {
    private lateinit var chiTietSP:ImageView
    private lateinit var backToHome:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_danh_sach_san_pham)
        setControl()
        setEvent()
    }
    private fun setControl(){
        chiTietSP = findViewById(R.id.chiTietSP)
        backToHome = findViewById(R.id.backHome)
    }
    private fun setEvent(){
        chiTietSP.setOnClickListener {
            val intent = Intent(this, ChiTietSanPham::class.java)
            startActivity(intent)
        }
        backToHome.setOnClickListener {
            onBackPressed()
        }
    }
}