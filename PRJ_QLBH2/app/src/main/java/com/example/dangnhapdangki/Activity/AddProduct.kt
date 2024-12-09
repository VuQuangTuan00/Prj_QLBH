package com.example.dangnhapdangki.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dangnhapdangki.Database.DonViDBHelper
import com.example.dangnhapdangki.Database.LoaiSanPhamDBHelper
import com.example.dangnhapdangki.Database.SanPhamDBHelper
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.ActivityAddProductBinding


class AddProduct : AppCompatActivity() {
    private lateinit var binding:ActivityAddProductBinding
    private lateinit var dsLoaiSP: ArrayList<LoaiSanPham>
    private lateinit var dsDonViSP: ArrayList<DonVi>
    private lateinit var dbDonViHelper: DonViDBHelper
    private lateinit var dbLoaiSPHelper: LoaiSanPhamDBHelper
    private lateinit var dbSanPhamHelper: SanPhamDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_product)
        setControl()
        setEvent()
        setupSpinners()
    }
    private fun setEvent(){

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnThemSanPham.setOnClickListener {
            val intent = Intent(this, TrangChu::class.java)
            startActivity(intent)
            val tenSanPham = binding.edtTenSanPham.text.toString()
            val soLuong = binding.edtSoLuong.text.toString().toIntOrNull() ?: 0

            val gia = binding.edtGia.text.toString().toDoubleOrNull() ?: 0.0
            val thongTin = binding.edtThongTin.text.toString()

            val selectedLoaiSanPham = binding.spLoaiSanPham.selectedItem.toString()
            val selectedDonVi = binding.spDonVi.selectedItem.toString()

            val loaiSanPhamID = dsLoaiSP.firstOrNull { it.tenLoai_sp == selectedLoaiSanPham }
            val donViID = dsDonViSP.firstOrNull { it.tenDonVi_sp == selectedDonVi }

            if (loaiSanPhamID != null && donViID != null) {
                // Lưu sản phẩm mới
                val sanPham = SanPham(0,"", tenSanPham,loaiSanPhamID, soLuong,donViID, gia, thongTin)
                dbSanPhamHelper.insertSanPham(sanPham)
                Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Vui lòng chọn loại sản phẩm và đơn vị!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setControl(){
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbDonViHelper = DonViDBHelper(this)
        dbLoaiSPHelper = LoaiSanPhamDBHelper(this)
        dbSanPhamHelper = SanPhamDBHelper(this)
        dsDonViSP = ArrayList(dbDonViHelper.getAllDonVi())
        dsLoaiSP = ArrayList(dbLoaiSPHelper.getAllLoaiSanPham())
    }
    private fun setupSpinners() {
        // Dữ liệu cho Spinner Loại Sản Phẩm
        val loaiSanPhamAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            dsLoaiSP.map { it.tenLoai_sp }
        )
        loaiSanPhamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spLoaiSanPham.adapter = loaiSanPhamAdapter

        // Dữ liệu cho Spinner Đơn Vị
        val donViAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            dsDonViSP.map { it.tenDonVi_sp }
        )
        donViAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spDonVi.adapter = donViAdapter
    }
}