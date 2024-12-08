package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dangnhapdangki.Database.DonViDBHelper
import com.example.dangnhapdangki.Database.LoaiSanPhamDBHelper
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.ActivityAddProductBinding


class AddProduct : AppCompatActivity() {
    private lateinit var binding:ActivityAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_product)
        setControl()
        setEvent()

    }
    private fun setEvent(){

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnThemSanPham.setOnClickListener {
            val intent = Intent(this, TrangChu::class.java)
            startActivity(intent)
            Toast.makeText(this,"Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setControl(){
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}