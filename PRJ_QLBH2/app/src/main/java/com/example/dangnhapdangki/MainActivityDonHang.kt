package com.example.dangnhapdangki

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.prj_qlbh.R

class MainActivityDonHang : AppCompatActivity() {
    private lateinit var ivBackLSDH: ImageView
    private lateinit var lvDonHang: ListView

    private var maKH: String = "001" // Mã khách hàng mặc định
    private val dsDonHang = ArrayList<DonHang>()
    private lateinit var donHangAdapter: DonHangAdapter
    private lateinit var dbHelper: DataBaseDonHang



    //Danh sách giỏ hàng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_don_hang)

        // Khởi tạo database helper
        dbHelper = DataBaseDonHang(this)

        setControl()
        setEvent()
    }

    private fun setEvent() {
        ivBackLSDH.setOnClickListener {
            onBackPressed()
        }

        // Khởi tạo danh sách đơn hàng từ cơ sở dữ liệu


        donHangAdapter = DonHangAdapter(this, R.layout.donhang_adapter, dsDonHang)
        lvDonHang.adapter = donHangAdapter

        loadData()
    }

    private fun setControl() {
        ivBackLSDH = findViewById(R.id.ivBackLSDH)
        lvDonHang = findViewById(R.id.lvDonHang)
    }

    private fun loadData() {
        // Xóa dữ liệu cũ trước khi thêm mới
        dsDonHang.clear()

        // Lấy danh sách đơn hàng từ database theo maKH
        val donHangsFromDb = dbHelper.getDonHangByMaKH(maKH)
        dsDonHang.addAll(donHangsFromDb)

        // Thông báo cập nhật danh sách
        donHangAdapter.notifyDataSetChanged()
    }
}
