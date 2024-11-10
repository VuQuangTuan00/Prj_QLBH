package vn.edu.tdc.qlbh

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.qlbh.R

class AddProduct : AppCompatActivity() {
    private lateinit var imgBack:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_product)
        setControl()
        setEvent()

    }
    private fun setEvent(){
        var intent = Intent(this, MainActivity::class.java)
        imgBack.setOnClickListener {
            startActivity(intent)
        }
    }
    private fun setControl(){
        imgBack = findViewById(R.id.imageView1)
    }
}