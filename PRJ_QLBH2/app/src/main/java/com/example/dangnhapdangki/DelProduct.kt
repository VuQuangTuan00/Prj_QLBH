package com.example.dangnhapdangki

import android.app.AlertDialog
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

class DelProduct : AppCompatActivity() {
    private lateinit var imgBack: ImageView
    private lateinit var btnDel: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_del_product)
        setControl()
        setEvent()

    }
    private fun setEvent(){
        imgBack.setOnClickListener {
            onBackPressed()
        }
        btnDel.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }
    private fun setControl(){
        imgBack = findViewById(R.id.imageDelBack)
        btnDel = findViewById(R.id.btnDel)
    }
    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)


        builder.setTitle("Xác nhận xóa sản phẩm")
            .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?")
            .setPositiveButton("Có") { dialog, id ->
                val intent = Intent(this, TrangChu::class.java)
                startActivity(intent)
                Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show() // Hiển thị Toast
            }
            .setNegativeButton("Không") { dialog, id ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}