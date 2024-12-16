package com.example.dangnhapdangki.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dangnhapdangki.Adapter.AdapterItemDSSP
import com.example.dangnhapdangki.Adapter.SuKienChuyenTrangUpdate
import com.example.dangnhapdangki.Database.DonViDBHelper
import com.example.dangnhapdangki.Database.LoaiSanPhamDBHelper
import com.example.dangnhapdangki.Database.SanPhamDBHelper
import com.example.demo_recycleview.Adapter.AdapterLoaiSanPham
import com.example.demo_recycleview.Adapter.AdapterSanPham
import com.example.demo_recycleview.Adapter.OnLoaiSanPhamClickListener
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.ActivityDanhSachSanPhamBinding

class DanhSachSanPham : AppCompatActivity(), SuKienChuyenTrangUpdate {
    private lateinit var binding:ActivityDanhSachSanPhamBinding
    private lateinit var dsSP: ArrayList<SanPham>
    private lateinit var dsLoaiSP: ArrayList<LoaiSanPham>
    private lateinit var dsDonViSP: ArrayList<DonVi>
    private lateinit var dbDonViHelper: DonViDBHelper
    private lateinit var dbLoaiSPHelper: LoaiSanPhamDBHelper
    private lateinit var dbSanPhamHelper: SanPhamDBHelper
    private lateinit var search:EditText
    private lateinit var adapterSP: AdapterSanPham
    companion object {
        const val REQUEST_CODE_UPDATE_PRODUCT = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_danh_sach_san_pham)
        setControl()
        setEvent()
    }

    private fun setControl(){
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
            setupSanPhamRecyclerView()
        } else {
            Toast.makeText(this, "Không có loại sản phẩm nào!", Toast.LENGTH_SHORT).show()
        }
        // search

    }
    private fun setEvent(){
        binding.backHome.setOnClickListener {
            onBackPressed()
        }

    }
    private fun setupLoaiSanPhamRecyclerView() {
        if (dsLoaiSP.isNotEmpty()) {
            val adapterLSP = AdapterLoaiSanPham(dsLoaiSP,object : OnLoaiSanPhamClickListener {
                override fun onLoaiSanPhamClick(loaiSanPham: LoaiSanPham) {
                    filterSanPhamByLoai(loaiSanPham)
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
    private fun filterSanPhamByLoai(loaiSanPham: LoaiSanPham) {
        if (::adapterSP.isInitialized) {
            val filteredList = dsSP.filter { it.idLoai_sp.idLoai_sp == loaiSanPham.idLoai_sp}
            if (filteredList.isNotEmpty()) {
                adapterSP.updateData(ArrayList(filteredList))
            } else {
                Toast.makeText(this, "Không có sản phẩm thuộc loại: ${loaiSanPham.tenLoai_sp}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupSanPhamRecyclerView() {
        if (dsSP.isNotEmpty()) {
            val adapterSP = AdapterItemDSSP(dsSP,dsLoaiSP,dsDonViSP)
            adapterSP.SuKienChuyenTrangUpdate = this
            binding.rvSanPham.apply {
                adapter = adapterSP
                layoutManager = LinearLayoutManager(
                    this@DanhSachSanPham,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        } else {
            Toast.makeText(this, "Không có Sản phẩm nào!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun chuyenTrang(view: View?, sanPham: SanPham) {
        val intent = Intent(this, UpdateProduct::class.java)
        intent.putExtra("sanPham", sanPham)
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

                    // Xóa sản phẩm khỏi danh sách và cập nhật RecyclerView
                    dsSP.removeAt(position)
                    binding.rvSanPham.adapter?.notifyItemRemoved(position)
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
                    binding.rvSanPham.adapter?.notifyItemChanged(index)
                }
            }
        }
    }
}