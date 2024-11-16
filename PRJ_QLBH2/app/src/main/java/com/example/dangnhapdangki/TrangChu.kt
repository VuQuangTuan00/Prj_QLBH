package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.prj_qlbh.R
import com.google.android.material.navigation.NavigationView

class TrangChu : AppCompatActivity() {
    // Khai báo biến dưới dạng var
    private var drawerLayout: DrawerLayout? = null
    private var menuIcon: ImageView? = null
    private var navigationView: NavigationView? = null

    private var homeIcon:ImageView? = null
    private var cartIcon:ImageView? = null
    private var favoriteIcon:ImageView? = null
    private var noticeIcon:ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trang_chu)
        setControl()
        setEvent()
    }

    private fun setControl() {
        drawerLayout = findViewById(R.id.drawerLayout)
        menuIcon = findViewById(R.id.imageView)
        navigationView = findViewById(R.id.navView)
        cartIcon = findViewById(R.id.cart_icon)
        favoriteIcon = findViewById(R.id.favourite_icon)
        noticeIcon = findViewById(R.id.notice_icon)
    }
    private fun setEvent() {
        // Thiết lập sự kiện click cho menuIcon để mở NavigationView
        menuIcon?.setOnClickListener {
            drawerLayout?.openDrawer(navigationView!!)
        }
        cartIcon?.setOnClickListener {
            val intent = Intent(this, MainActivityGioHang::class.java)
            startActivity(intent)
        }
        favoriteIcon?.setOnClickListener {
            val intent = Intent(this, MainActivityGioHang::class.java)
            startActivity(intent)
        }
        noticeIcon?.setOnClickListener {
            val intent = Intent(this, MainActivityGioHang::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện khi chọn các item trong NavigationView
        navigationView?.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navThoat -> {
                    finish()
                    true
                }

                R.id.navThemSP -> {
                    val intent = Intent(this, AddProduct::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navXoaSP -> {
                    val intent = Intent(this, DelProduct::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navSuaSP -> {
                    val intent = Intent(this, UpdateProduct::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navDSSP -> {
                    val intent = Intent(this, DanhSachSanPham::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }
}
