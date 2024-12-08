package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.ActivityChiTietSanPhamBinding
import com.example.prj_qlbh.databinding.ActivityTrangChuBinding

class ChiTietSanPham : AppCompatActivity() {
    private lateinit var binding:ActivityChiTietSanPhamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chi_tiet_san_pham)
        setControl()
        setEvent()
    }
    private fun setControl(){
        binding = ActivityChiTietSanPhamBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    private fun setEvent(){
        val sanPham = intent.getParcelableExtra<SanPham>("sanPham")
        binding.tenSanPham.text = sanPham?.ten_sp
        binding.giaSanPham.text = sanPham?.gia_sp.toString()
        binding.thongTinSanPham.text = sanPham?.thongTin
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

    }
}