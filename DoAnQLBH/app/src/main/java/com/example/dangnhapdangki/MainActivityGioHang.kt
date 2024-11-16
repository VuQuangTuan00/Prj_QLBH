package com.example.dangnhapdangki
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivityGioHang : AppCompatActivity() {
    private lateinit var lvDanhSachGioHang: ListView
    private lateinit var btnDatHang: Button


    private val dsGioHang =  ArrayList<GioHang>()
    private lateinit var gioHangAdapter: GioHangAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_gio_hang)
        setControl();
        setEvent();
    }

    private fun setControl() {
        lvDanhSachGioHang = findViewById(R.id.lvGioHang)
        btnDatHang = findViewById(R.id.btnDatHang)
    }

    private fun setEvent() {
        khoiTao();
        gioHangAdapter = GioHangAdapter(this, R.layout.giohang_adapter, dsGioHang)
        lvDanhSachGioHang.adapter = gioHangAdapter


        btnDatHang.setOnClickListener {
            // Tạo Intent để chuyển từ MainActivity sang SecondActivity
            val intent = Intent(this, DatHang::class.java)
            startActivity(intent) // Khởi chạy Intent
        }
    }
    fun khoiTao() {
        dsGioHang.add(GioHang("001", "Dầu ăn Simply đậu nành 5L", "Dầu ăn", 136000 * 1.0, 1));
        dsGioHang.add(GioHang("002", "Snack vị tảo biển Hàn Quôc" +
                "nướng Lays gói 152g", "Snack", 16000 * 1.0, 1));
    }
}