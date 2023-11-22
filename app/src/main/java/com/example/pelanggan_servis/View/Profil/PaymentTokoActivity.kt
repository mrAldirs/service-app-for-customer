package com.example.pelanggan_servis.View.Profil

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.databinding.ProfilPaymentTokoBinding

class PaymentTokoActivity : AppCompatActivity() {
    private lateinit var b: ProfilPaymentTokoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ProfilPaymentTokoBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        b.btnBack.setOnClickListener { onBackPressed() }
        b.btnKembali.setOnClickListener {
            onBackPressed()
            Toast.makeText(this, "Terima kasih telah memuji kami! ^_^", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}