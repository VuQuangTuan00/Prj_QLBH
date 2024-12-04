package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dangnhapdangki.MainActivityDonHang
import com.example.dangnhapdangki.MainActivityNapDiem
import com.example.dangnhapdangki.SmallActivity
import com.example.prj_qlbh.R

class MainActivityDatHang : AppCompatActivity() {
    private lateinit var tvNapDiem: TextView
    private lateinit var ivBackDH: ImageView
    private lateinit var btnDatHang: Button
    private lateinit var tvPhuongThucThanhToan: TextView
    private lateinit var linearLayoutPhuongThuc: LinearLayout
    private lateinit var tvChuyenKhoan: TextView
    private lateinit var tvTienMat: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dathang)



        getControl()
        getEvent()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            // Nhận dữ liệu từ SmallActivity
//            val result = data?.getStringExtra("phuongThucThanhToan") ?: ""
//            tvPhuongThucThanhToan.text = result // Cập nhật giá trị
//        }
//    }

    fun getControl() {
        tvNapDiem = findViewById(R.id.tvNapDiem)
        ivBackDH = findViewById(R.id.ivBackDH)
        btnDatHang = findViewById(R.id.btnDatHang)
        tvPhuongThucThanhToan = findViewById(R.id.tvPhuongThucThanhToan)
        linearLayoutPhuongThuc = findViewById(R.id.linearLayoutPhuongThuc)
        tvTienMat = findViewById(R.id.tvTienMat)
        tvChuyenKhoan = findViewById(R.id.tvChuyenKhoan)

    }

    fun getEvent() {
        linearLayoutPhuongThuc.visibility = View.GONE
        // Đặt sự kiện onClick cho tvNapDiem
        tvNapDiem.setOnClickListener {
            // Tạo Intent để chuyển từ MainActivity sang SecondActivity
            val intent = Intent(this, MainActivityNapDiem::class.java)
            startActivity(intent) // Khởi chạy Intent
        }

        ivBackDH.setOnClickListener {
            onBackPressed()
        }

        btnDatHang.setOnClickListener {
            val intent = Intent(this, MainActivityDonHang::class.java)
            startActivity(intent) // Khởi chạy Intent
        }

        // Mở SmallActivity khi nhấn nút
        tvPhuongThucThanhToan.setOnClickListener {
            linearLayoutPhuongThuc.visibility = View.VISIBLE
            tvPhuongThucThanhToan.visibility = View.VISIBLE
        }

        tvChuyenKhoan.setOnClickListener {
            linearLayoutPhuongThuc.visibility = View.GONE
            tvPhuongThucThanhToan.visibility = View.VISIBLE
            tvPhuongThucThanhToan.text = "Chuyển Khoản"
        }

        tvTienMat.setOnClickListener {
            linearLayoutPhuongThuc.visibility = View.GONE
            tvPhuongThucThanhToan.visibility = View.VISIBLE
            tvPhuongThucThanhToan.text = "Tiền Mặt"
        }
    }
}