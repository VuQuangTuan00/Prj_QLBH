package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prj_qlbh.R

class UpdateProduct : AppCompatActivity() {
    private lateinit var imgBack: ImageView
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
    }
    private fun setControl(){
        imgBack = findViewById(R.id.imageUpdateBack)
    }
}