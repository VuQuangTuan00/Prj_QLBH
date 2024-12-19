package com.example.dangnhapdangki.Activity

import AdapterSanPham
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dangnhapdangki.Activity.TrangChu.Companion
import com.example.dangnhapdangki.Adapter.AdapterItemDSSP
import com.example.dangnhapdangki.Adapter.SuKienChuyenTrangUpdate
import com.example.dangnhapdangki.Database.DonViDBHelper
import com.example.dangnhapdangki.Database.LoaiSanPhamDBHelper
import com.example.dangnhapdangki.Database.SanPhamDBHelper
import com.example.demo_recycleview.Adapter.AdapterLoaiSanPham

import com.example.demo_recycleview.Adapter.OnLoaiSanPhamClickListener
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.ActivityDanhSachSanPhamBinding

class DanhSachSanPham : AppCompatActivity(), SuKienChuyenTrangUpdate {
    private lateinit var binding: ActivityDanhSachSanPhamBinding
    private lateinit var dsSP: ArrayList<SanPham>
    private lateinit var dsLoaiSP: ArrayList<LoaiSanPham>
    private lateinit var dsDonViSP: ArrayList<DonVi>
    private lateinit var dbDonViHelper: DonViDBHelper
    private lateinit var dbLoaiSPHelper: LoaiSanPhamDBHelper
    private lateinit var dbSanPhamHelper: SanPhamDBHelper
    private lateinit var search: EditText
    private lateinit var adapterLSP:AdapterLoaiSanPham


    companion object {
        var tuKhoa: String = ""
        var loaiSanPham = LoaiSanPham(-1, "")
        lateinit var adapterSP: AdapterSanPham
        const val REQUEST_CODE_UPDATE_PRODUCT = 1001
        var hinhSP = ""
        var sanPhamBanHang: SanPham =
            SanPham(0, "", "", LoaiSanPham(0, ""), 0, DonVi(0, ""), 0.0, "")
        var sanPhamCapNhap: SanPham =
            SanPham(0, "", "", LoaiSanPham(0, ""), 0, DonVi(0, ""), 0.0, "")
    }

    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 5000L // Cập nhật mỗi 5 giây


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_danh_sach_san_pham)
        setControl()
        setEvent()
    }

    private fun setControl() {

        binding = ActivityDanhSachSanPhamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbSanPhamHelper = SanPhamDBHelper(this)
        dbDonViHelper = DonViDBHelper(this)
        dbLoaiSPHelper = LoaiSanPhamDBHelper(this)

        search = findViewById(R.id.searchEditText)

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
        // search

    }

    private fun setEvent() {
        binding.backHome.setOnClickListener {
            onBackPressed()
        }

        binding.tvLoaiSPAll.visibility = View.GONE
        binding.tvLoaiSPAll.setOnClickListener {
            loaiSanPham = LoaiSanPham(-1, "")
            adapterLSP.selectedPosition = -1
            adapterLSP.tvLoaiSP.setBackgroundResource(R.drawable.editext_bg)
            binding.tvLoaiSPAll.setBackgroundResource(R.drawable.brown_bg)
            loadData()
        }

        binding.searchEditText.addTextChangedListener { text ->
            val query = text.toString()
            tuKhoa = text.toString()
            filterSanPham(query)

        }

    }

    private fun setupLoaiSanPhamRecyclerView() {
        if (dsLoaiSP.isNotEmpty()) {
             adapterLSP = AdapterLoaiSanPham(dsLoaiSP, object : OnLoaiSanPhamClickListener {
                override fun onLoaiSanPhamClick(loaiSanPham: LoaiSanPham) {
                    filterSanPhamByLoai(loaiSanPham)
                    DanhSachSanPham.loaiSanPham =
                        LoaiSanPham(loaiSanPham.idLoai_sp, loaiSanPham.tenLoai_sp)
                    binding.tvLoaiSPAll.setBackgroundResource(R.drawable.editext_bg)
                    binding.tvLoaiSPAll.visibility = View.VISIBLE
                }

            })

            binding.rvLoaiSanPham.apply {
                adapter = adapterLSP
                layoutManager = LinearLayoutManager(
                    this@DanhSachSanPham,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        } else {
            Toast.makeText(this, "Không có loại sản phẩm nào!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filterSanPham(query: String) {
        val filteredList = dsSP.filter { it.ten_sp.contains(query, ignoreCase = true) }
        val parentLayout = binding.gridLayoutSanPham

        if (filteredList.isNotEmpty()) {
            adapterSP = AdapterSanPham(ArrayList(filteredList), dsLoaiSP, dsDonViSP, this)
            adapterSP.populateLinearLayout(parentLayout)
        } else {
            parentLayout.removeAllViews() // Xóa toàn bộ nội dung trước đó
            //Toast.makeText(this, "Không tìm thấy sản phẩm phù hợp!", Toast.LENGTH_SHORT).show()
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

    private fun setupSanPhamGridView() {
        val parentLayout = binding.gridLayoutSanPham

        if (dsSP.isNotEmpty()) {
            // Khởi tạo adapter và thêm vào LinearLayout
            adapterSP = AdapterSanPham(dsSP, dsLoaiSP, dsDonViSP, this)
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
        hinhSP = sanPham.img_sp

        // Khởi tạo lại đối tượng với các giá trị mới


        val intent = Intent(this, UpdateProduct::class.java)
        startActivityForResult(intent, REQUEST_CODE_UPDATE_PRODUCT)
    }


    override fun xoaSanPham(sanPham: SanPham) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Xóa sản phẩm")
            .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
            .setPositiveButton("Có") { dialogInterface, _ ->
                val position = dsSP.indexOfFirst { it.id_sanPham == sanPham.id_sanPham }
                if (position != -1) {
                    // Xóa sản phẩm khỏi cơ sở dữ liệu
                    dbSanPhamHelper.deleteProduct(sanPham.id_sanPham)
                    Toast.makeText(this, "Sản phẩm đã được xóa", Toast.LENGTH_SHORT).show()

                    val parentLayout = binding.gridLayoutSanPham
                    dsSP.clear() // Xóa dữ liệu cũ
                    dsSP.addAll(dbSanPhamHelper.getAllProducts()) // Thêm dữ liệu mới
                    adapterSP.populateLinearLayout(parentLayout) //

                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("Không") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE_PRODUCT && resultCode == RESULT_OK) {
            val updatedSanPham = data?.getParcelableExtra<SanPham>("updatedSanPham")
            if (updatedSanPham != null) {
                // Tìm và cập nhật sản phẩm trong danh sách
                val index = dsSP.indexOfFirst { it.id_sanPham == updatedSanPham.id_sanPham }
                if (index != -1) {
                    dsSP[index] = updatedSanPham
                    val parentLayout = binding.gridLayoutSanPham
                    dsSP.clear() // Xóa dữ liệu cũ
                    dsSP.addAll(dbSanPhamHelper.getAllProducts()) // Thêm dữ liệu mới
                    adapterSP.populateLinearLayout(parentLayout) //
                }
            }
        }
    }


    fun loadData() {
        if (!tuKhoa.equals("")) {
            filterSanPham(tuKhoa)
        }
        else if(adapterLSP.selectedPosition != -1){
            filterSanPhamByLoai(loaiSanPham)
        }
        else {
            loadData2()
        }
    }

    fun loadData2(){
        val dbHelper = SanPhamDBHelper(this)
        val updatedList = dbHelper.getAllProducts()

        val parentLayout = binding.gridLayoutSanPham
        if (updatedList.isNotEmpty()) {
            dsSP.clear()
            dsSP.addAll(updatedList)

            // Cập nhật lại danh sách hiển thị
           adapterSP = AdapterSanPham(dsSP, dsLoaiSP, dsDonViSP, this)
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
            loadData()
            handler.postDelayed(this, updateInterval)
        }
    }
}