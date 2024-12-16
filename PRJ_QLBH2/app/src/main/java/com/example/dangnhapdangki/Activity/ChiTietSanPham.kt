package com.example.dangnhapdangki.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dangnhapdangki.ChuyenDoiHinhAnh
import com.example.dangnhapdangki.Database.DataBaseGioHang
import com.example.dangnhapdangki.Database.LoaiSanPhamDBHelper
import com.example.dangnhapdangki.Model.GioHang
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.ActivityChiTietSanPhamBinding

class ChiTietSanPham : AppCompatActivity() {
    private lateinit var binding: ActivityChiTietSanPhamBinding
    private val dbGioHang: DataBaseGioHang by lazy { DataBaseGioHang(this) }
    private val dbLoaiSanPham: LoaiSanPhamDBHelper by lazy { LoaiSanPhamDBHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chi_tiet_san_pham)
        setControl()
        setEvent()
    }

    private fun setControl() {
        binding = ActivityChiTietSanPhamBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setEvent() {
        val sanPham = intent.getParcelableExtra<SanPham>("sanPham")
        binding.tenSanPham.text = "Tên sản phẩm: " + sanPham?.ten_sp
        binding.giaSanPham.text = "Giá: " + sanPham?.gia_sp.toString()
        binding.thongTinSanPham.text = sanPham?.thongTin
        try {
            val chuyenDoiHinhAnh = ChuyenDoiHinhAnh()
            val hinhSP = chuyenDoiHinhAnh.chuyenStringSangByte(sanPham?.img_sp)
            val hinhSPBM = chuyenDoiHinhAnh.chuyenByteSangBitMap(hinhSP)
            binding.imgSanPham.setImageBitmap(hinhSPBM)

        } catch (e: Exception) {
            e.printStackTrace() // Hoặc sử dụng log để ghi lại lỗi
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnThemGioHang.setOnClickListener {
            // Hiển thị hộp thoại xác nhận
            AlertDialog.Builder(this) // 'this' là Context hiện tại
                .setTitle("Xác nhận") // Tiêu đề
                .setMessage("Bạn có chắc chắn muốn thêm sản phẩm này vào giỏ hàng?") // Nội dung thông báo
                .setPositiveButton("Yes") { dialog, which ->
                    val tenLoaiSanPham = dbLoaiSanPham.getTenLoaiSanPhamById(sanPham?.idLoai_sp?.idLoai_sp ?: 0)
                    val gioHang1 = GioHang(
                        "" + sanPham?.id_sanPham,
                        sanPham?.ten_sp.toString(),
                        "" + tenLoaiSanPham,
                        136000 * 1.0,
                        1,
                        "" + sanPham?.img_sp,
                        0,
                        DangNhap.maKH
                    )
                    dbGioHang.insertGioHang(gioHang1)
                    Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivityGioHang::class.java)
                    startActivity(intent) // Khởi chạy Intent
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }


    }
}