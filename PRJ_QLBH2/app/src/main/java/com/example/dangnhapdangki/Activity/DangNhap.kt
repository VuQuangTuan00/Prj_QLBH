package com.example.dangnhapdangki.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DataBaseUser
import com.example.prj_qlbh.R

class DangNhap : AppCompatActivity() {
    private lateinit var edtTenNguoiDung : EditText
    private  lateinit var edtPassword: EditText
    private lateinit var btnDangKi: TextView
    private lateinit var btnDangNhap: Button
    private lateinit var databaseHelper: DataBaseUser
    companion object {
        var maKH: String = "001" //MaKhachHang
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dang_nhap)
        setControl()
        setEvent()
        databaseHelper = DataBaseUser(this)
    }

    private fun setEvent() {
        btnDangNhap.setOnClickListener(){
            val tenNguoiDung = edtTenNguoiDung.text.toString()
            val mathhau = edtPassword.text.toString()

            if (tenNguoiDung.isNotEmpty() && mathhau.isNotEmpty()) {
                if (databaseHelper.checkUser(tenNguoiDung, mathhau)) {
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    val intent = Intent(this, TrangChu::class.java)
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
        btnDangKi.setOnClickListener(){
            val intent = Intent(this, DangKi::class.java)
            startActivity(intent);
        }
    }

    private fun setControl() {
        edtTenNguoiDung = findViewById(R.id.edtTenNguoiDung)
        edtPassword = findViewById(R.id.edtPassword)
        btnDangKi = findViewById(R.id.tvDangKi)
        btnDangNhap = findViewById(R.id.btnDangNhap)
    }
}