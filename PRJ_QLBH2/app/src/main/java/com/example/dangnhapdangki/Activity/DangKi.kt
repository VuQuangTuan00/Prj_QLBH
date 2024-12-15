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


class DangKi : AppCompatActivity() {
    private lateinit var edtTenNguoiDung : EditText
    private  lateinit var edtPassword: EditText
    private lateinit var edtPasswordComfirm: EditText
    private lateinit var tvDangNhap: TextView
    private lateinit var btnDangKi: Button
    private lateinit var databaseHelper: DataBaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dang_ki)
        setControl()
        setEvent()
        databaseHelper = DataBaseUser(this)
    }

    private fun setEvent() {
        btnDangKi.setOnClickListener(){
            xuLyDangKy();
        }
        tvDangNhap.setOnClickListener(){
            val intent = Intent(this, DangNhap::class.java)
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
        var tenNguoiDung = edtTenNguoiDung.text.toString();
        var matkhau = edtPassword.text.toString();
        var xacminhmatkhau = edtPasswordComfirm.text.toString().trim();

        if (tenNguoiDung.isNotEmpty() && matkhau.isNotEmpty()&& matkhau.equals(xacminhmatkhau)) {
            databaseHelper.addUser(tenNguoiDung, matkhau)
            Toast.makeText(this, "Đăng ký thành công !", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Vui lòng kiểm tra lại thông tin vừa nhập!", Toast.LENGTH_SHORT).show()
        }
    }
}