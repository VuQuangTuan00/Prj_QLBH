import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.dangnhapdangki.Activity.ChiTietSanPham
import com.example.dangnhapdangki.Activity.TrangChu
import com.example.dangnhapdangki.Activity.TrangChu.Companion.sanPhamBanHang
import com.example.dangnhapdangki.ChuyenDoiHinhAnh
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.databinding.ItemSanphamBinding

class AdapterSanPhamGrid(
    private var dsSanPham: ArrayList<SanPham>,
    private val dsLoaiSP: ArrayList<LoaiSanPham>,
    private val dsDonViSP: ArrayList<DonVi>,
    private val context: Context
) : BaseAdapter() {

    var SuKienChuyenTrangChiTiet: SuKienChuyenTrangChiTiet? = null

    override fun getCount(): Int = dsSanPham.size

    override fun getItem(position: Int): SanPham = dsSanPham[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemSanphamBinding
        val view: View

        if (convertView == null) {
            binding = ItemSanphamBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as ItemSanphamBinding
        }

        val curDSSP = dsSanPham[position]
        val curLoaiSP = dsLoaiSP.find { it.idLoai_sp == curDSSP.idLoai_sp.idLoai_sp }
        val curDonVi = dsDonViSP.find { it.idDonVi_sp == curDSSP.idDonVi_sp.idDonVi_sp }

        val giaText = "${curDSSP.gia_sp}vnd / ${curDSSP.soLuong_sp}${curDSSP.idDonVi_sp.tenDonVi_sp}"
        val spannable = SpannableString(giaText)

        val indexSlash = giaText.indexOf("/")
        if (indexSlash != -1) {
            spannable.setSpan(
                RelativeSizeSpan(0.5f),
                indexSlash + 2,
                giaText.length,
                0
            )
        }

        if (curLoaiSP != null && curDonVi != null) {
            binding.tvTenSp.text = curDSSP.ten_sp
            binding.tvGia.text = spannable
            binding.tvLoaiSp.text = curDSSP.idLoai_sp.tenLoai_sp

            val chuyenDoiHinhAnh = ChuyenDoiHinhAnh()
            try {
                val hinhByte: ByteArray = chuyenDoiHinhAnh.chuyenStringSangByte(curDSSP.img_sp, context)
                val hinhBitMap: Bitmap = chuyenDoiHinhAnh.chuyenByteSangBitMap(hinhByte, context)
                binding.imgSanPham.setImageBitmap(hinhBitMap)
            } catch (e: Exception) {
                Log.e("AdapterSanPhamGrid", "Lỗi chuyển đổi hình ảnh", e)
            }
        }



        binding.linearLayoutSanPham.setOnClickListener{

            TrangChu.sanPhamBanHang = SanPham(
                id_sanPham = curDSSP.id_sanPham,
                img_sp = curDSSP.img_sp,
                ten_sp = curDSSP.ten_sp,
                idLoai_sp = curDSSP.idLoai_sp,
                soLuong_sp = curDSSP.soLuong_sp,
                idDonVi_sp = curDSSP.idDonVi_sp,
                gia_sp = curDSSP.gia_sp,
                thongTin = curDSSP.thongTin
            )
            val intent = Intent(context, ChiTietSanPham::class.java)
            context.startActivity(intent)
        }




        return view
    }

    fun updateData(newList: ArrayList<SanPham>) {
        dsSanPham = newList
        notifyDataSetChanged()
    }
}

interface SuKienChuyenTrangChiTiet {

}