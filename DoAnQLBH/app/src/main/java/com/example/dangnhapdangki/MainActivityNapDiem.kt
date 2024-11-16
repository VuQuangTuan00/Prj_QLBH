package com.example.dangnhapdangki

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivityNapDiem : AppCompatActivity() {
    private lateinit var  ivBack: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_nap_diem)
        setControl();
        setEvent();
    }

    private fun setEvent() {
        // Đặt sự kiện onClick cho ivBack
        ivBack.setOnClickListener {
            onBackPressed()

        }
    }

    private fun setControl() {
        ivBack = findViewById(R.id.ivBack)
    }
}