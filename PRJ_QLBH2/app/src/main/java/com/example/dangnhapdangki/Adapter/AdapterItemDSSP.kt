package com.example.dangnhapdangki.Adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dangnhapdangki.ChuyenDoiHinhAnh
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.databinding.ActivityItemDsspBinding
import com.example.prj_qlbh.databinding.ItemSanphamBinding

class AdapterItemDSSP(
    var dsSanPham: ArrayList<SanPham>,
    var dsLoaiSP:ArrayList<LoaiSanPham>,
    var dsDonViSP:ArrayList<DonVi>,
) :
    RecyclerView.Adapter<AdapterItemDSSP.DSSanPhamViewHolder>() {
    private lateinit var binding: ActivityItemDsspBinding
    var SuKienChuyenTrangUpdate: SuKienChuyenTrangUpdate? = null
    inner class DSSanPhamViewHolder(val binding: ActivityItemDsspBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DSSanPhamViewHolder {
        binding = ActivityItemDsspBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DSSanPhamViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DSSanPhamViewHolder, position: Int) {
        val curDSSP =  dsSanPham[position]
        val curLoaiSP =  dsLoaiSP.find { it.idLoai_sp == curDSSP.idLoai_sp.idLoai_sp }
        val curDonVi =  dsDonViSP.find { it.idDonVi_sp == curDSSP.idDonVi_sp.idDonVi_sp }

        val giaText = "${curDSSP.gia_sp}vnd / ${curDSSP.soLuong_sp}${curDSSP.idDonVi_sp.tenDonVi_sp}"

        val spannable = SpannableString(giaText)

        val indexSlash = giaText.indexOf("/")
        if (indexSlash != -1) {
            spannable.setSpan(RelativeSizeSpan(0.5f), indexSlash + 2, giaText.length, 0) // 0.8f để làm chữ nhỏ đi 80% so với chữ trước "/"
        }
        if (curLoaiSP != null && curDonVi != null){
            holder.binding.tvTenSp.text = curDSSP.ten_sp
            holder.binding.tvGia.text = spannable
            holder.binding.tvLoaiSp.text = curDSSP.idLoai_sp.tenLoai_sp
        }
        holder.itemView.setOnClickListener {
            SuKienChuyenTrangUpdate?.chuyenTrang(it ,curDSSP)
        }
        holder.binding.btnMuaSp.setOnClickListener {
            SuKienChuyenTrangUpdate?.xoaSanPham(curDSSP)
        }

        var chuyenDoiHinhAnh: ChuyenDoiHinhAnh = ChuyenDoiHinhAnh()

        try {
            val context = holder.itemView.context
            val hinhByte: ByteArray = chuyenDoiHinhAnh.chuyenStringSangByte(curDSSP.img_sp,context)
            val hinhBitMap: Bitmap = chuyenDoiHinhAnh.chuyenByteSangBitMap(hinhByte,context)
            holder.binding.imgSanPham.setImageBitmap(hinhBitMap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun getItemCount(): Int {
        return if (dsSanPham.isNotEmpty()) {
            dsSanPham.size
        } else {
            0
        }
    }
}

interface SuKienChuyenTrangUpdate {
    fun chuyenTrang(view: View?, sanPham: SanPham)
    fun xoaSanPham(sanPham:SanPham)
}