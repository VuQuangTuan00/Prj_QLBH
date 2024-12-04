package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.prj_qlbh.R

class DatHang : AppCompatActivity() {
    private lateinit var tvNapDiem: TextView
    private lateinit var ivBackDH: ImageView
    private lateinit var btnDatHang: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dat_hang)

        // Mở SmallActivity khi nhấn nút
        findViewById<TextView>(R.id.tvPhuongThucThanhToan).setOnClickListener {
            startActivity(Intent(this, SmallActivity::class.java))
        }

        setControl();
        setEvent();
    }

    private fun setEvent() {
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
    }

    private fun setControl() {
        tvNapDiem = findViewById(R.id.tvNapDiem)
        ivBackDH = findViewById(R.id.ivBackDH)
        btnDatHang = findViewById(R.id.btnDatHang)

    }
}