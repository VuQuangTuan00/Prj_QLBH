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
import com.example.prj_qlbh.databinding.ActivityUpdateProductBinding

class UpdateProduct : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProductBinding

    private lateinit var dsLoaiSP: ArrayList<LoaiSanPham>
    private lateinit var dsDonViSP: ArrayList<DonVi>
    private lateinit var dbDonViHelper: DonViDBHelper
    private lateinit var dbLoaiSPHelper: LoaiSanPhamDBHelper
    private lateinit var dbSanPhamHelper: SanPhamDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_product)
        setControl()
        setEvent()
        setupSpinners()
    }
    private fun setEvent(){
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSuaSanPham.setOnClickListener {
            val tenSanPham = binding.edtTenSanPham.text.toString().trim()
            val giaSanPham = binding.edtGia.text.toString().toDoubleOrNull()
            val thongTinSanPham = binding.edtThongTin.text.toString().trim()
            val soLuongSanPham = binding.edtSoLuong.text.toString().toIntOrNull()
            val loaiIndex = binding.spLoaiSP.selectedItemPosition
            val loaiSanPham = if (loaiIndex >= 0) dsLoaiSP[loaiIndex] else null

            val donViIndex = binding.spDonViSP.selectedItemPosition
            val donViSanPham = if (donViIndex >= 0) dsDonViSP[donViIndex] else null

            // Kiểm tra thông tin nhập vào
            if (tenSanPham.isEmpty() || giaSanPham == null || soLuongSanPham == null || loaiSanPham == null || donViSanPham == null) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lấy sản phẩm hiện tại từ Intent
            val sanPham = intent.getParcelableExtra<SanPham>("sanPham")
            if (sanPham != null) {
                // Cập nhật thông tin sản phẩm
                sanPham.ten_sp = tenSanPham
                sanPham.gia_sp = giaSanPham
                sanPham.thongTin = thongTinSanPham
                sanPham.soLuong_sp = soLuongSanPham
                sanPham.idLoai_sp = loaiSanPham
                sanPham.idDonVi_sp = donViSanPham

                // Thực hiện cập nhật vào cơ sở dữ liệu
                val result = dbSanPhamHelper.updateProduct(sanPham)
                if (result) {
                    Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show()

                    // Trả dữ liệu sản phẩm đã chỉnh sửa về màn hình trước
                    val returnIntent = Intent().apply {
                        putExtra("updatedSanPham", sanPham)
                    }
                    setResult(RESULT_OK, returnIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val sanPham = intent.getParcelableExtra<SanPham>("sanPham")
        binding.edtTenSanPham.setText(sanPham?.ten_sp)
        binding.edtGia.setText(sanPham?.gia_sp?.toString())
        binding.edtThongTin.setText(sanPham?.thongTin)
        val loaiIndex = dsLoaiSP.indexOfFirst { it.idLoai_sp == sanPham?.idLoai_sp?.idLoai_sp }
        binding.spLoaiSP.setSelection(if (loaiIndex != -1) loaiIndex else 0)


        val donViIndex = dsDonViSP.indexOfFirst { it.idDonVi_sp == sanPham?.idDonVi_sp?.idDonVi_sp }
        binding.spDonViSP.setSelection(if (donViIndex != -1) donViIndex else 0)
        binding.edtSoLuong.setText(sanPham?.soLuong_sp.toString())
    }
    private fun setControl(){
        binding = ActivityUpdateProductBinding.inflate(layoutInflater)
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
        binding.spLoaiSP.adapter = loaiSanPhamAdapter

        // Dữ liệu cho Spinner Đơn Vị
        val donViAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            dsDonViSP.map { it.tenDonVi_sp }
        )
        donViAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spDonViSP.adapter = donViAdapter
    }
}