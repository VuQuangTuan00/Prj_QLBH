package com.example.dangnhapdangki

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivityDonHang : AppCompatActivity() {
    private lateinit var ivBackLSDH: ImageView
    private lateinit var lvDonHang: ListView

    private val dsDonHang = ArrayList<DonHang>()
    private lateinit var donHangAdapter: DonHangAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_don_hang)
        setControl();
        setEvent();
    }

    private fun setEvent() {
        ivBackLSDH.setOnClickListener {
            onBackPressed()
        }
        khoiTao()
        donHangAdapter = DonHangAdapter(this, R.layout.donhang_adapter, dsDonHang)
        lvDonHang.adapter = donHangAdapter
    }

    private fun setControl() {
        ivBackLSDH = findViewById(R.id.ivBackLSDH);
        lvDonHang = findViewById(R.id.lvDonHang);
    }

    fun khoiTao() {
        dsDonHang.add(
            DonHang(
                "SP001",
                "Dầu ăn Simply đậu nành 5L",
                "Dầu ăn",
                "123 Đường ABC, Quận 1, TP.HCM",
                "2024-11-10 14:00",
                "Đã thanh toán (chuyển khoản)",
                136000.0,
                2
            )
        )

        dsDonHang.add(
            DonHang(
                "SP001",
                "Snack khoai tây",
                "Snack",
                "123 Đường ABC, Quận 1, TP.HCM",
                "2024-11-10 14:00",
                "Chưa thanh toán (tiền mặt)",
                13000.0,
                2
            )
        )

    }
}