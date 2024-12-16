//package com.example.dangnhapdangki.Adapter
//
//import android.os.Handler
//import android.os.Looper
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.prj_qlbh.databinding.ActivityTrangChuBinding.ItemBannerBinding
//import com.example.dangnhapdangki.Model.Banner
//
//class BannerAdapter(private val bannerList: List<Banner>) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
//
//    private var currentPosition = 0
//    private val handler = Handler(Looper.getMainLooper())
//    private val updateBannerRunnable = Runnable {
//        if (currentPosition < bannerList.size - 1) {
//            currentPosition++
//        } else {
//            currentPosition = 0
//        }
//        notifyDataSetChanged()
//        handler.postDelayed(updateBannerRunnable, 3000) // 3000ms = 3 giây
//    }
//
//    // Tạo ViewHolder cho Banner
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
//        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return BannerViewHolder(binding)
//    }
//
//    // Liên kết dữ liệu vào view (mỗi banner)
//    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
//        val banner = bannerList[position]
//        holder.bind(banner)
//    }
//
//    // Trả về số lượng banner
//    override fun getItemCount(): Int {
//        return bannerList.size
//    }
//
//    // ViewHolder cho Banner
//    inner class BannerViewHolder(private val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(banner: Banner) {
//            // Sử dụng Glide để tải ảnh từ drawable vào ImageView
//            val into = Glide.with(itemView.context)
//                .load(banner.imageResId) // Tải ảnh từ resource drawable
//                .into(binding.bannerImageView)
//        }
//    }
//
//    // Bắt đầu vòng lặp tự động sau khi RecyclerView được gán adapter
//    fun startBannerAutoSlide() {
//        handler.postDelayed(updateBannerRunnable, 3000)
//    }
//
//    // Dừng vòng lặp khi không cần thiết
//    fun stopBannerAutoSlide() {
//        handler.removeCallbacks(updateBannerRunnable)
//    }
//}