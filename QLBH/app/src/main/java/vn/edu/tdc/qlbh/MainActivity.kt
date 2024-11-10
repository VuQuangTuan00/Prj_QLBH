package vn.edu.tdc.qlbh
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.qlbh.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    // Khai báo biến dưới dạng var
    private var drawerLayout: DrawerLayout? = null
    private var menuIcon: ImageView? = null
    private var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setControl()
        setEvent()
    }

    private fun setControl() {
        drawerLayout = findViewById(R.id.drawerLayout)
        menuIcon = findViewById(R.id.imageView)
        navigationView = findViewById(R.id.navView)
    }

    private fun setEvent() {
        // Thiết lập sự kiện click cho menuIcon để mở NavigationView
        menuIcon?.setOnClickListener {
            drawerLayout?.openDrawer(navigationView!!)
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

                else -> false
            }
        }
    }
}
