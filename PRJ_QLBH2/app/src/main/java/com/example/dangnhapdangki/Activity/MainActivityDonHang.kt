package com.example.dangnhapdangki.Activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dangnhapdangki.Database.DataBaseDonHang
import com.example.dangnhapdangki.Model.DonHang
import com.example.dangnhapdangki.Adapter.DonHangAdapter
import com.example.prj_qlbh.R

class MainActivityDonHang : AppCompatActivity() {
    private lateinit var ivBackLSDH: ImageView
    private lateinit var lvDonHang: ListView
    private lateinit var tvXoaTatCa: TextView

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


        // Khởi tạo danh sách đơn hàng từ cơ sở dữ liệu


        donHangAdapter = DonHangAdapter(
            this,
            R.layout.donhang_adapter,
            dsDonHang,
            dbHelper
        )
        lvDonHang.adapter = donHangAdapter

        loadData()

        ivBackLSDH.setOnClickListener {
            onBackPressed()
        }

        tvXoaTatCa.setOnClickListener {
            // Hiển thị hộp thoại xác nhận trước khi xóa
            val dialog = AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa tất cả đơn hàng?")
                .setPositiveButton("Có") { _, _ ->
                    // Nếu người dùng chọn "Có", thực hiện xóa tất cả đơn hàng
                    dbHelper.deleteAllDonHang()
                    loadData() // Tải lại dữ liệu sau khi xóa
                    Toast.makeText(this, "Đã xóa tất cả đơn hàng!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Không") { dialogInterface, _ ->
                    // Nếu người dùng chọn "Không", đóng hộp thoại
                    dialogInterface.dismiss()
                }
            dialog.show()
        }

    }

    private fun setControl() {
        ivBackLSDH = findViewById(R.id.ivBackLSDH)
        lvDonHang = findViewById(R.id.lvDonHang)
        tvXoaTatCa = findViewById(R.id.tvXoaTatCa)
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
