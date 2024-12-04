package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.prj_qlbh.R

class MainActivityGioHang : AppCompatActivity() {
    private lateinit var lvDanhSachGioHang: ListView
    private lateinit var btnDatHang: Button
    private lateinit var imgBack: ImageView
    private lateinit var btn_del_giohang: Button
    private lateinit var tvTienHang: TextView
    private lateinit var tvTongTien: TextView
    private lateinit var tvTongDonHang: TextView

    private val dbGioHang: DataBaseGioHang by lazy { DataBaseGioHang(this) }
    private val dsGioHang = ArrayList<GioHang>()
    private lateinit var gioHangAdapter: GioHangAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_gio_hang)
        setControl()
        setEvent()
    }

    private fun setControl() {
        lvDanhSachGioHang = findViewById(R.id.lvGioHang)
        btnDatHang = findViewById(R.id.btnDatHang)
        imgBack = findViewById(R.id.ivBack)
        btn_del_giohang = findViewById(R.id.btn_del_giohang)

        tvTienHang = findViewById(R.id.tvTienHang)
        tvTongTien = findViewById(R.id.tvTongTien)
        tvTongDonHang = findViewById(R.id.tvTongDonHang)
    }

    private fun setEvent() {
        // Khởi tạo adapter trước khi sử dụng

        gioHangAdapter = GioHangAdapter(this, R.layout.giohang_adapter, dsGioHang, this)
        lvDanhSachGioHang.adapter = gioHangAdapter

        //dbGioHang.deleteAllGioHang()

        dbGioHang.updateAllDaChonToZero()

        tingTongTien()
        //khoiTao() // Thêm dữ liệu vào danh sách
        loadGioHang()

        btnDatHang.setOnClickListener {
            // Tạo Intent để chuyển từ MainActivity sang SecondActivity
            val intent = Intent(this, DatHang::class.java)
            startActivity(intent) // Khởi chạy Intent
        }

        imgBack.setOnClickListener {
            onBackPressed()
        }

        btn_del_giohang.setOnClickListener {
            // Kiểm tra xem có sản phẩm nào đã được chọn không
            if (dbGioHang.isAnyProductSelected()) {
                // Nếu có ít nhất một sản phẩm đã chọn, yêu cầu xác nhận xóa
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Bạn có chắc chắn muốn xóa tất cả sản phẩm đã chọn?")
                    .setCancelable(false)
                    .setPositiveButton("Có") { dialog, id ->
                        // Nếu người dùng chọn "Có", thực hiện xóa sản phẩm đã chọn
                        dbGioHang.deleteGioHangDaChon()
                        loadGioHang() // Cập nhật lại giỏ hàng
                    }
                    .setNegativeButton("Không") { dialog, id ->
                        // Nếu người dùng chọn "Không", đóng dialog mà không làm gì cả
                        dialog.dismiss()
                    }

                // Hiển thị AlertDialog
                val alert = builder.create()
                alert.show()
            } else {
                // Nếu không có sản phẩm nào được chọn, hiển thị thông báo
                Toast.makeText(this, "Không có sản phẩm nào được chọn để xóa", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun khoiTao() {

        var stringBase64: StringBase64 = StringBase64()
        // Create GioHang objects and insert into the database
        val gioHang1 = GioHang(
            "001",
            "Dầu ăn Simply đậu nành 5L",
            "Dầu ăn",
            136000 * 1.0,
            1,
            stringBase64.base64_anh2,
            0
        )
        val gioHang2 = GioHang(
            "002",
            "Snack vị tảo biển Hàn Quôc nướng Lays gói 152g",
            "Snack",
            16000 * 1.0,
            1,
            stringBase64.base64_anh1,
            0
        )

        // Insert into the database
        dbGioHang.insertGioHang(gioHang1)
        dbGioHang.insertGioHang(gioHang2)

        // Load updated data from the database
        loadGioHang()
    }

    private fun loadGioHang() {
        // Lấy danh sách giỏ hàng từ cơ sở dữ liệu
        dsGioHang.clear()
        dsGioHang.addAll(dbGioHang.getAllGioHang())
        gioHangAdapter.notifyDataSetChanged()
    }


    fun tingTongTien() {
        val totalPrice = dbGioHang.getTotalPriceOfSelectedProducts()
        val formattedPrice =
            String.format("%,.2f", totalPrice) // Định dạng với dấu phân cách và 2 chữ số thập phân

// Cập nhật giao diện
        tvTongTien.text = "$formattedPrice đ"
        tvTienHang.text = "$formattedPrice đ"
        tvTongDonHang.text = "$formattedPrice đ"


    }
}
