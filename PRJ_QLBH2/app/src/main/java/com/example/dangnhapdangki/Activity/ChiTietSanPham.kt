package com.example.dangnhapdangki.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.ActivityChiTietSanPhamBinding

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