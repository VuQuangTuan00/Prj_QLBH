package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DangNhap : AppCompatActivity() {
    private lateinit var edtTenNguoiDung : EditText
    private  lateinit var edtPassword: EditText
    private lateinit var btnThoat: Button
    private lateinit var btnDangKi: Button
    private lateinit var btnDangNhap: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dang_nhap)
        setcontrol()
        setEvent()
    }

    private fun setEvent() {
        btnThoat.setOnClickListener(){
            finish();
        }
        btnDangNhap.setOnClickListener(){
            xuLyDangNhap();
            // val intent = Intent(this,Home::class.java)
            // startActivity(intent);
        }
        btnDangKi.setOnClickListener(){
            val intent = Intent(this,DangKi::class.java)
            startActivity(intent);
        }
    }

    private fun setcontrol() {
        edtTenNguoiDung = findViewById(R.id.edtTenNguoiDung)
        edtPassword = findViewById(R.id.edtPassword)
        btnThoat = findViewById(R.id.btnThoat)
        btnDangKi = findViewById(R.id.btnDangKi)
        btnDangNhap = findViewById(R.id.btnDangNhap)
    }

    private fun xuLyDangNhap(){
        val tenNguoiDung = edtTenNguoiDung.text.toString();
        val matKhau = edtPassword.text.toString();

        //kiểm tra tồn tại
        if (tenNguoiDung.isEmpty() || matKhau.isEmpty()){
            Toast.makeText(this,"vui lòng nhập tên người dùng",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
        }
    }
}