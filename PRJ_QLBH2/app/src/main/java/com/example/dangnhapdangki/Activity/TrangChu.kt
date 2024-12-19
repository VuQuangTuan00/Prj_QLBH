package com.example.dangnhapdangki.Activity

import AdapterSanPhamGrid
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dangnhapdangki.Activity.DanhSachSanPham.Companion
import com.example.dangnhapdangki.ChuyenDoiHinhAnh
import com.example.dangnhapdangki.Database.DonViDBHelper
import com.example.dangnhapdangki.Database.LoaiSanPhamDBHelper
import com.example.dangnhapdangki.Database.SanPhamDBHelper
import com.example.demo_recycleview.Adapter.AdapterLoaiSanPham
import com.example.demo_recycleview.Adapter.OnLoaiSanPhamClickListener
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.ActivityTrangChuBinding
import com.example.prj_qlbh.databinding.ItemSanphamBinding

class TrangChu : AppCompatActivity() {

    private lateinit var binding: ActivityTrangChuBinding
    private lateinit var dsSP: ArrayList<SanPham>
    private lateinit var dsLoaiSP: ArrayList<LoaiSanPham>
    private lateinit var dsDonViSP: ArrayList<DonVi>
    private lateinit var dbDonViHelper: DonViDBHelper
    private lateinit var dbLoaiSPHelper: LoaiSanPhamDBHelper
    private lateinit var dbSanPhamHelper: SanPhamDBHelper
    private lateinit var adapterLSP:AdapterLoaiSanPham


    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 5000L // Cập nhật mỗi 5 giây


    companion object {
        var tuKhoa: String = ""
        var loaiSanPham = LoaiSanPham(-1, "")

        lateinit var adapterSP: AdapterSanPhamGrid
        var manHinh: String = "TrangChu"
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
            setupSanPhamGridView()
        } else {
            Toast.makeText(this, "Không có loại sản phẩm nào!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setEvent() {
        binding.searchEditText.addTextChangedListener { text ->
            val query = text.toString()
            tuKhoa = text.toString()
            filterSanPham(query)
        }

        binding.tvLoaiSPAll.visibility = View.GONE

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
                    manHinh = "QuanTri"
                    val intent = Intent(this, DanhSachSanPham::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        binding.tvLoaiSPAll.setOnClickListener {
            loaiSanPham = LoaiSanPham(-1, "")
            adapterLSP.selectedPosition = -1
            adapterLSP.tvLoaiSP.setBackgroundResource(R.drawable.editext_bg)
            binding.tvLoaiSPAll.setBackgroundResource(R.drawable.brown_bg)
            loadData()
        }
    }

    private fun setupLoaiSanPhamRecyclerView() {
        if (dsLoaiSP.isNotEmpty()) {
             adapterLSP = AdapterLoaiSanPham(dsLoaiSP, object : OnLoaiSanPhamClickListener {
                override fun onLoaiSanPhamClick(loaiSanPham: LoaiSanPham) {
                    filterSanPhamByLoai(loaiSanPham)
                    Companion.loaiSanPham =
                        LoaiSanPham(loaiSanPham.idLoai_sp, loaiSanPham.tenLoai_sp)
                    binding.tvLoaiSPAll.setBackgroundResource(R.drawable.editext_bg)
                    binding.tvLoaiSPAll.visibility = View.VISIBLE
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
        if (::dbSanPhamHelper.isInitialized) {
            // Lấy toàn bộ danh sách sản phẩm từ cơ sở dữ liệu
            val dsSP = dbSanPhamHelper.getAllProducts()

            // Lọc sản phẩm dựa trên loại sản phẩm
            val filteredList = dsSP.filter { it.idLoai_sp.idLoai_sp == loaiSanPham.idLoai_sp }


            if (filteredList.isNotEmpty()) {
                // Cập nhật dữ liệu hiển thị
                adapterSP.updateData(ArrayList(filteredList), binding.gridLayoutSanPham)

            } else {
                // Thông báo nếu không có sản phẩm thuộc loại
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
        val parentLayout = binding.gridLayoutSanPham

        if (filteredList.isNotEmpty()) {
            adapterSP = AdapterSanPhamGrid(ArrayList(filteredList), dsLoaiSP, dsDonViSP, this)
            adapterSP.populateLinearLayout(parentLayout)
        } else {
            parentLayout.removeAllViews() // Xóa toàn bộ nội dung trước đó
            //Toast.makeText(this, "Không tìm thấy sản phẩm phù hợp!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupSanPhamGridView() {
        val parentLayout = binding.gridLayoutSanPham

        if (dsSP.isNotEmpty()) {
            // Khởi tạo adapter và thêm vào LinearLayout
            adapterSP = AdapterSanPhamGrid(dsSP, dsLoaiSP, dsDonViSP, this)
            adapterSP.populateLinearLayout(parentLayout)

            // Xử lý sự kiện click vào từng item nếu cần
            for (i in 0 until parentLayout.childCount) {
                val child = parentLayout.getChildAt(i)
                child.setOnClickListener {
                    val sanPham = dsSP[i]
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
                }
            }
        } else {
            // Thông báo nếu danh sách sản phẩm trống
            Toast.makeText(this, "Không có sản phẩm nào!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateSanPhamList() {

        if (!tuKhoa.equals("")) {
            filterSanPham(tuKhoa)
        }
        else if(adapterLSP.selectedPosition != -1){
            filterSanPhamByLoai(loaiSanPham)
        }
        else {
            loadData()
        }

    }
    fun loadData() {
        val dbHelper = SanPhamDBHelper(this)
        val updatedList = dbHelper.getAllProducts()

        val parentLayout = binding.gridLayoutSanPham
        if (updatedList.isNotEmpty()) {
            dsSP.clear()
            dsSP.addAll(updatedList)

            // Cập nhật lại danh sách hiển thị
            adapterSP = AdapterSanPhamGrid(dsSP, dsLoaiSP, dsDonViSP, this)
            adapterSP.populateLinearLayout(parentLayout)
        } else {
            parentLayout.removeAllViews() // Xóa toàn bộ nội dung
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
