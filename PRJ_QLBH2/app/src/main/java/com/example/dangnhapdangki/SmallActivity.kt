package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prj_qlbh.R

class SmallActivity : AppCompatActivity() {
    private  lateinit var tvChuyenKhoan: TextView
    private  lateinit var tvTienMat: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_small)

        // Thiết lập kích thước cho Activity
        val params: WindowManager.LayoutParams = window.attributes
        params.width = 600 // Chiều rộng
        params.height = 800 // Chiều cao
        window.attributes = params


        setControl()
        setEvrnt()
    }

    fun setControl() {
        tvChuyenKhoan = findViewById(R.id.tvChuyenKhoan)
        tvTienMat = findViewById(R.id.tvTienMat)
    }

    fun setEvrnt() {
        // Đóng Activity khi nhấn nút "Đóng"
        findViewById<Button>(R.id.close_button).setOnClickListener {
            finish()
        }
        tvChuyenKhoan.setOnClickListener{
            val intent = Intent()
            intent.putExtra("phuongThucThanhToan", "Chuyển khoản") // Gửi kết quả về
            setResult(RESULT_OK, intent)
            finish() // Đóng SmallActivity
        }

        tvTienMat.setOnClickListener{
            val intent = Intent()
            intent.putExtra("phuongThucThanhToan", "Tiền mặt") // Gửi kết quả về
            setResult(RESULT_OK, intent)
            finish() // Đóng SmallActivity
        }
    }
}