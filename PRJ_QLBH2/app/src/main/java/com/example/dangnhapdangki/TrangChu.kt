package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dangnhapdangki.Database.DonViDBHelper
import com.example.dangnhapdangki.Database.LoaiSanPhamDBHelper
import com.example.demo_recycleview.Adapter.AdapterLoaiSanPham
import com.example.demo_recycleview.Adapter.AdapterSanPham
import com.example.demo_recycleview.Adapter.SuKienChuyenTrangChiTiet
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.ActivityTrangChuBinding
import com.google.android.material.navigation.NavigationView

class TrangChu : AppCompatActivity(), SuKienChuyenTrangChiTiet {

    private lateinit var binding: ActivityTrangChuBinding
    private lateinit var dsSP: ArrayList<SanPham>
    private lateinit var dsLoaiSP: ArrayList<LoaiSanPham>
    private lateinit var dsDonViSP: ArrayList<DonVi>
    private lateinit var dbDonViHelper: DonViDBHelper
    private lateinit var dbLoaiSPHelper: LoaiSanPhamDBHelper
    private var adapterSanPham: AdapterSanPham? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setControl()

        dbLoaiSPHelper = LoaiSanPhamDBHelper(this)
        dbDonViHelper = DonViDBHelper(this)

        // Lấy dữ liệu từ cơ sở dữ liệu
        dsDonViSP = ArrayList(dbDonViHelper.getAllDonVi())
        dsLoaiSP = ArrayList(dbLoaiSPHelper.getAllLoaiSanPham())

        // Kiểm tra dữ liệu và cấu hình RecyclerView nếu có dữ liệu

        if (dsLoaiSP.isNotEmpty()) {
            setupLoaiSanPhamRecyclerView()
        } else {
            Toast.makeText(this, "Không có loại sản phẩm nào!", Toast.LENGTH_SHORT).show()
        }

        // Nếu cần hiển thị sản phẩm, bạn có thể bổ sung vào đây
        // setupSanPhamRecyclerView()

        setEvent()
    }

    private fun setControl() {
        binding = ActivityTrangChuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setEvent() {
        binding.bugerNav?.setOnClickListener {
            binding.drawerLayout?.openDrawer(binding.navView!!)
        }

        binding.navView?.setNavigationItemSelectedListener { item ->
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

    private fun setupLoaiSanPhamRecyclerView() {
        if (dsLoaiSP.isNotEmpty()) {
            val adapterLSP = AdapterLoaiSanPham(dsLoaiSP)
            binding.rvLoaiSanPham.apply {
                adapter = adapterLSP
                layoutManager = LinearLayoutManager(
                    this@TrangChu,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        } else {
            Toast.makeText(this, "Không có loại sản phẩm nào!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun chuyenTrang(view: View?, sanPham: SanPham) {
        val intent = Intent(this, ChiTietSanPham::class.java)
        intent.putExtra("sanPham", sanPham)
        startActivity(intent)
    }
}
