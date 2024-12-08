package com.example.demo_recycleview.Adapter
import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.databinding.ItemSanphamBinding


class AdapterSanPham(
    var dsSanPham: ArrayList<SanPham>,
    var dsLoaiSP:ArrayList<LoaiSanPham>,
    var dsDonViSP:ArrayList<DonVi>,
) :
    RecyclerView.Adapter<AdapterSanPham.SanPhamViewHolder>() {
    private lateinit var binding: ItemSanphamBinding
    var SuKienChuyenTrangChiTiet: SuKienChuyenTrangChiTiet? = null


    inner class SanPhamViewHolder(val binding: ItemSanphamBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SanPhamViewHolder {
        binding = ItemSanphamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SanPhamViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: SanPhamViewHolder, position: Int) {
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
            SuKienChuyenTrangChiTiet?.chuyenTrang(it ,curDSSP)
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

interface SuKienChuyenTrangChiTiet {
    fun chuyenTrang(view: View? , sanPham:SanPham)
}
