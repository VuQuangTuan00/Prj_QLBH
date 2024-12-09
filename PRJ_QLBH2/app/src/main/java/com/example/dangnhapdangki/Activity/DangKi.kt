package com.example.dangnhapdangki.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
        var xacminhmatkhau = edtPasswordComfirm.text.toString();

        //kiểm tra tồn tại và đồng nhất mật khẩu
        if (tenNguoiDung.isEmpty() || matkhau.isEmpty() || xacminhmatkhau.isEmpty() || !matkhau.equals(xacminhmatkhau))
        {
            if (tenNguoiDung.isEmpty() || matkhau.isEmpty() || xacminhmatkhau.isEmpty())
            {
                Toast.makeText(this,"Các trường dữ liệu không được bỏ trống !",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"mật khẩu và xác minh mật khẩu không trùng nhau !",Toast.LENGTH_SHORT).show()
            }
            return;
        }
        //nếu đúng
        var intent = Intent(this, DangNhap::class.java);
        startActivity(intent);
        Toast.makeText(this,"Đăng kí thành công !",Toast.LENGTH_SHORT).show();

    }
}