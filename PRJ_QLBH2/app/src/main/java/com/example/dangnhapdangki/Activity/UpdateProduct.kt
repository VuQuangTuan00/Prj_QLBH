package com.example.dangnhapdangki.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.dangnhapdangki.ChuyenDoiHinhAnh
import com.example.dangnhapdangki.Database.DataBaseGioHang
import com.example.dangnhapdangki.Database.DonViDBHelper
import com.example.dangnhapdangki.Database.LoaiSanPhamDBHelper
import com.example.dangnhapdangki.Database.SanPhamDBHelper
import com.example.dangnhapdangki.StringBase64
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.ActivityUpdateProductBinding
import java.io.ByteArrayOutputStream
import java.io.IOException

class UpdateProduct : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProductBinding

    private lateinit var dsLoaiSP: ArrayList<LoaiSanPham>
    private lateinit var dsDonViSP: ArrayList<DonVi>
    private lateinit var dbDonViHelper: DonViDBHelper
    private lateinit var dbLoaiSPHelper: LoaiSanPhamDBHelper
    private lateinit var dbSanPhamHelper: SanPhamDBHelper

    private val dbSanPhamDBHelper: SanPhamDBHelper by lazy { SanPhamDBHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_product)
        setControl()
        setEvent()
        setupSpinners()
    }

    private fun setEvent() {
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgSanPham.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        })

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
            val sanPham = DanhSachSanPham.sanPhamBanHang
            if (sanPham != null) {
                // Cập nhật thông tin sản phẩm
                sanPham.ten_sp = tenSanPham
                sanPham.gia_sp = giaSanPham
                sanPham.thongTin = thongTinSanPham
                sanPham.soLuong_sp = soLuongSanPham
                sanPham.idLoai_sp = loaiSanPham
                sanPham.idDonVi_sp = donViSanPham
                try {
                    if (byteArrayHinh.isNotEmpty()) {
                        val chuyenDoiHinhAnh = ChuyenDoiHinhAnh()
                        val hinhSP = chuyenDoiHinhAnh.chuyenByteSangChuoi(byteArrayHinh, this)
                        sanPham.img_sp = hinhSP
                    } else {
                        val chuyenDoiHinhAnh = ChuyenDoiHinhAnh()
                        val hinhByte: ByteArray =
                            chuyenDoiHinhAnh.chuyenStringSangByte(DanhSachSanPham.hinhSP, this)
                        val hinhSP = chuyenDoiHinhAnh.chuyenByteSangChuoi(hinhByte, this)
                        sanPham.img_sp = hinhSP
                    }

                } catch (e: Exception) {
                    e.printStackTrace() // Hoặc sử dụng log để ghi lại lỗi
                }


                // Thực hiện cập nhật vào cơ sở dữ liệu
                val result = dbSanPhamHelper.updateProduct(sanPham)
                if (result) {


                    // Trả dữ liệu sản phẩm đã chỉnh sửa về màn hình trước
                    var kq: Boolean = dbSanPhamDBHelper.updateProduct(sanPham)
                    if (kq == true) {
                        DanhSachSanPham.tvALLSanPham.visibility = View.VISIBLE
                        DanhSachSanPham.adapterLSP.selectedPosition = -1
                        try {
                            DanhSachSanPham.adapterLSP.tvLoaiSP.setBackgroundResource(R.drawable.editext_bg)
                        } catch (e: Exception) {

                        }

                        DanhSachSanPham.tuKhoa = ""
                        DanhSachSanPham.searchSanPham.setText("")

                        DanhSachSanPham.loaiSanPham = LoaiSanPham(-1, "")
                        DanhSachSanPham.tvALLSanPham.setBackgroundResource(R.drawable.brown_bg)

                        Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            this,
                            "Cập nhật sản phẩm không thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    finish()
                } else {
                    Toast.makeText(this, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val sanPham = DanhSachSanPham.sanPhamBanHang
        binding.edtTenSanPham.setText(DanhSachSanPham.sanPhamBanHang.ten_sp)
        binding.edtGia.setText(sanPham?.gia_sp?.toString())
        binding.edtThongTin.setText(sanPham?.thongTin)
        val loaiIndex = dsLoaiSP.indexOfFirst { it.idLoai_sp == sanPham?.idLoai_sp?.idLoai_sp }
        binding.spLoaiSP.setSelection(if (loaiIndex != -1) loaiIndex else 0)


        val donViIndex = dsDonViSP.indexOfFirst { it.idDonVi_sp == sanPham?.idDonVi_sp?.idDonVi_sp }
        binding.spDonViSP.setSelection(if (donViIndex != -1) donViIndex else 0)
        binding.edtSoLuong.setText(sanPham?.soLuong_sp.toString())

        var chuyenDoiHinhAnh = ChuyenDoiHinhAnh()
        var stringBase64 = StringBase64()
        if (DanhSachSanPham.hinhSP.isNullOrEmpty()) {
            val hinhByte: ByteArray =
                chuyenDoiHinhAnh.chuyenStringSangByte(stringBase64.base64_anh2, this)
            val hinhBitMap: Bitmap = chuyenDoiHinhAnh.chuyenByteSangBitMap(hinhByte, this)
            binding.imgSanPham.setImageBitmap(hinhBitMap)
        } else {
            val hinhByte: ByteArray =
                chuyenDoiHinhAnh.chuyenStringSangByte(DanhSachSanPham.hinhSP, this)
            val hinhBitMap: Bitmap = chuyenDoiHinhAnh.chuyenByteSangBitMap(hinhByte, this)
            binding.imgSanPham.setImageBitmap(hinhBitMap)
        }


    }

    private fun setControl() {
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

        // Lấy adapter từ Spinner
        val adapter = binding.spLoaiSP.adapter

        // Lấy giá trị cần tìm
        val tenLoaiSPHienTai = DanhSachSanPham.sanPhamBanHang?.idLoai_sp?.tenLoai_sp

        // Duyệt qua các item trong adapter
        var viTriLoaiSP = -1
        for (i in 0 until adapter.count) {
            val item = adapter.getItem(i).toString() // Lấy item tại vị trí i
            if (item == tenLoaiSPHienTai) {
                viTriLoaiSP = i
                break // Thoát vòng lặp nếu tìm thấy
            }
        }

        // Kiểm tra nếu tìm thấy giá trị
        if (viTriLoaiSP >= 0) {
            // Đặt Spinner vào đúng vị trí
            binding.spLoaiSP.setSelection(viTriLoaiSP)
        }


        // Lấy adapter từ Spinner
        val adapterDonVi = binding.spDonViSP.adapter

// Lấy giá trị cần tìm
        val tenDonViHienTai = DanhSachSanPham.sanPhamBanHang?.idDonVi_sp?.tenDonVi_sp

// Duyệt qua các item trong adapter
        var viTriDonViSP = -1
        for (i in 0 until adapterDonVi.count) {
            val item = adapterDonVi.getItem(i).toString() // Lấy item tại vị trí i
            if (item == tenDonViHienTai) {
                viTriDonViSP = i
                break // Thoát vòng lặp nếu tìm thấy
            }
        }

        // Kiểm tra nếu tìm thấy giá trị
        if (viTriDonViSP >= 0) {
            // Đặt Spinner vào đúng vị trí
            binding.spDonViSP.setSelection(viTriDonViSP)
        }


    }

    var byteArrayHinh: ByteArray = ByteArray(0)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val uri: Uri? = data.data
            binding.imgSanPham.setImageURI(uri)
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                byteArrayHinh = stream.toByteArray()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}