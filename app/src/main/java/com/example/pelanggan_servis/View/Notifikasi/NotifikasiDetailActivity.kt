package com.example.pelanggan_servis.View.Notifikasi

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.databinding.NotifikasiDetailActivityBinding

class NotifikasiDetailActivity : AppCompatActivity() {
    private lateinit var b: NotifikasiDetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = NotifikasiDetailActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        b.btnCloseNotifikasiDetail.setOnClickListener {
            onBackPressed()
        }
        b.btnTutupNotifikasiDetail.setOnClickListener {
            onBackPressed()
        }

        var paket : Bundle? = intent.extras
        b.dtJenisNotifikasiPelanggan.setText(paket?.getString("jenis").toString())
        b.dtJamtanggalNotifikasiPelanggan.setText(paket?.getString("jam").toString())
        b.dtTeksPesanNotifikasiPelanggan.setText(paket?.getString("teks").toString())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}