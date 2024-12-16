package com.example.dangnhapdangki.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dangnhapdangki.Activity.DanhSachSanPham.Companion
import com.example.dangnhapdangki.Database.DonViDBHelper
import com.example.dangnhapdangki.Database.LoaiSanPhamDBHelper
import com.example.dangnhapdangki.Database.SanPhamDBHelper
import com.example.demo_recycleview.Adapter.AdapterLoaiSanPham
import com.example.demo_recycleview.Adapter.AdapterSanPham
import com.example.demo_recycleview.Adapter.OnLoaiSanPhamClickListener
import com.example.demo_recycleview.Adapter.SuKienChuyenTrangChiTiet
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.ActivityTrangChuBinding

class TrangChu : AppCompatActivity(), SuKienChuyenTrangChiTiet {

    private lateinit var binding: ActivityTrangChuBinding
    private lateinit var dsSP: ArrayList<SanPham>
    private lateinit var dsLoaiSP: ArrayList<LoaiSanPham>
    private lateinit var dsDonViSP: ArrayList<DonVi>
    private lateinit var dbDonViHelper: DonViDBHelper
    private lateinit var dbLoaiSPHelper: LoaiSanPhamDBHelper
    private lateinit var dbSanPhamHelper: SanPhamDBHelper
    private lateinit var adapterSP: AdapterSanPham

    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 5000L // Cập nhật mỗi 5 giây

    companion object {
        var sanPhamBanHang: SanPham =
            SanPham(0, "", "", LoaiSanPham(0, ""), 0, DonVi(0, ""), 0.0, "")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setControl()
        setEvent()
    }

    private fun setControl() {
        binding = ActivityTrangChuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbSanPhamHelper = SanPhamDBHelper(this)
        dbDonViHelper = DonViDBHelper(this)
        dbLoaiSPHelper = LoaiSanPhamDBHelper(this)
//        dbSanPhamHelper.insertSampleProducts()

        // Lấy dữ liệu từ cơ sở dữ liệu
        dsSP = ArrayList(dbSanPhamHelper.getAllProducts())
        dsDonViSP = ArrayList(dbDonViHelper.getAllDonVi())
        dsLoaiSP = ArrayList(dbLoaiSPHelper.getAllLoaiSanPham())


        // Kiểm tra dữ liệu và cấu hình RecyclerView nếu có dữ liệu

        if (dsLoaiSP.isNotEmpty()) {
            setupLoaiSanPhamRecyclerView()
        } else {
            Toast.makeText(this, "Không có loại sản phẩm nào!", Toast.LENGTH_SHORT).show()
        }

        // Nếu cần hiển thị sản phẩm, bạn có thể bổ sung vào đây
        if (dsSP.isNotEmpty()) {
            setupSanPhamRecyclerView()
        } else {
            Toast.makeText(this, "Không có loại sản phẩm nào!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setEvent() {
        binding.searchEditText.addTextChangedListener { text ->
            val query = text.toString()
            filterSanPham(query)
        }

        binding.bugerNav?.setOnClickListener {
            binding.drawerLayout?.openDrawer(binding.navView!!)
        }
        binding.btNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.cart -> {
                    // Chuyển sang trang Cart
                    val intent = Intent(this, MainActivityGioHang::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
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
            val adapterLSP = AdapterLoaiSanPham(dsLoaiSP, object : OnLoaiSanPhamClickListener {
                override fun onLoaiSanPhamClick(loaiSanPham: LoaiSanPham) {
                    filterSanPhamByLoai(loaiSanPham)
                }

            })

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

    private fun filterSanPhamByLoai(loaiSanPham: LoaiSanPham) {
        if (::adapterSP.isInitialized) {
            val filteredList = dsSP.filter { it.idLoai_sp.idLoai_sp == loaiSanPham.idLoai_sp }
            if (filteredList.isNotEmpty()) {
                adapterSP.updateData(ArrayList(filteredList))
            } else {
                Toast.makeText(
                    this,
                    "Không có sản phẩm thuộc loại: ${loaiSanPham.tenLoai_sp}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun filterSanPham(query: String) {
        val filteredList = dsSP.filter { it.ten_sp.contains(query, ignoreCase = true) }
        if (filteredList.isNotEmpty()) {
            adapterSP.updateData(ArrayList(filteredList))
        } else {
            Toast.makeText(this, "Không tìm thấy sản phẩm phù hợp!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSanPhamRecyclerView() {
        if (dsSP.isNotEmpty()) {
            adapterSP = AdapterSanPham(dsSP, dsLoaiSP, dsDonViSP)
            adapterSP.SuKienChuyenTrangChiTiet = this
            binding.rvSanPham.apply {
                adapter = adapterSP
                layoutManager = LinearLayoutManager(
                    this@TrangChu,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        } else {
            Toast.makeText(this, "Không có Sản phẩm nào!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun chuyenTrang(view: View?, sanPham: SanPham) {
        sanPhamBanHang = SanPham(
            id_sanPham = sanPham.id_sanPham,
            img_sp = sanPham.img_sp,
            ten_sp = sanPham.ten_sp,
            idLoai_sp = sanPham.idLoai_sp,
            soLuong_sp = sanPham.soLuong_sp,
            idDonVi_sp = sanPham.idDonVi_sp,
            gia_sp = sanPham.gia_sp,
            thongTin = sanPham.thongTin
        )
        val intent = Intent(this, ChiTietSanPham::class.java)
        startActivity(intent)
    }


    private fun updateSanPhamList() {
        val dbHelper = SanPhamDBHelper(this)
        val updatedList = dbHelper.getAllProducts()
        if (updatedList.isNotEmpty()) {
            dsSP.clear()
            dsSP.addAll(updatedList)
            binding.rvSanPham.adapter?.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "Danh sách sản phẩm trống!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        handler.post(updateTask)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(updateTask)
    }

    private val updateTask = object : Runnable {
        override fun run() {
            updateSanPhamList()
            handler.postDelayed(this, updateInterval)
        }
    }
}
