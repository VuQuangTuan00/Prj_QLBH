package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prj_qlbh.R

class UpdateProduct : AppCompatActivity() {
    private lateinit var imgBack: ImageView
    private lateinit var btnUpdate: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_product)
        setControl()
        setEvent()
    }
    private fun setEvent(){
        imgBack.setOnClickListener {
            onBackPressed()
        }
        btnUpdate.setOnClickListener {
            val intent = Intent(this, TrangChu::class.java)
            startActivity(intent)
            Toast.makeText(this,"Sửa sản phẩm thành công", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setControl(){
        imgBack = findViewById(R.id.imageUpdateBack)
        btnUpdate = findViewById(R.id.btnUpdate)
    }
}