package com.example.dangnhapdangki

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prj_qlbh.R

class SmallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_small)

        // Thiết lập kích thước cho Activity
        val params: WindowManager.LayoutParams = window.attributes
        params.width = 600 // Chiều rộng
        params.height = 800 // Chiều cao
        window.attributes = params

        // Đóng Activity khi nhấn nút "Đóng"
        findViewById<Button>(R.id.close_button).setOnClickListener {
            finish()
        }
    }
}