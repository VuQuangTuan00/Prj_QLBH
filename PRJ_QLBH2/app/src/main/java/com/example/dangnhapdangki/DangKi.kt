package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.prj_qlbh.R


class DangKi : AppCompatActivity() {
    private lateinit var edtTenNguoiDung : EditText
    private  lateinit var edtPassword: EditText
    private lateinit var edtPasswordComfirm: EditText
    private lateinit var tvDangNhap: TextView
    private lateinit var btnDangKi: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dang_ki)
        setControl()
        setEvent()

    }

    private fun setEvent() {
        btnDangKi.setOnClickListener(){
            val intent = Intent(this,DangNhap::class.java)
            startActivity(intent);
        }
        tvDangNhap.setOnClickListener(){
            val intent = Intent(this,DangNhap::class.java)
            startActivity(intent);
        }
    }

    private fun setControl() {
        edtTenNguoiDung = findViewById(R.id.edtTenNguoiDung)
        edtPassword = findViewById(R.id.edtPassword)
        edtPasswordComfirm = findViewById(R.id.edtPasswordComfirm)
        tvDangNhap = findViewById(R.id.tvDangNhap)
        btnDangKi = findViewById(R.id.btnDangKi)
    }
    private fun xuLyDangKy(){

    }
}