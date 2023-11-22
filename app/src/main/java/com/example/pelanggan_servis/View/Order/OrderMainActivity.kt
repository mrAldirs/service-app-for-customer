package com.example.pelanggan_servis.View.Order

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pelanggan_servis.Helper.BaseApplication
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.Model.OrderModel
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.ViewModel.TransaksiViewModel
import com.example.pelanggan_servis.ViewModel.UserViewModel
import com.example.pelanggan_servis.databinding.OrderMainActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class OrderMainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var b: OrderMainActivityBinding
    private lateinit var uVM : UserViewModel
    private lateinit var tVM : TransaksiViewModel
    lateinit var preferences: SharedPreferencesHelper

    var jenisBarang = ""

    lateinit var fragmentOrderDetail: FragmentOrderDetail
    lateinit var ft : FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = OrderMainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        uVM = ViewModelProvider(this).get(UserViewModel::class.java)
        tVM = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        preferences = SharedPreferencesHelper(this)
        b.rgJenisBarangOrder.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rbJenisAC -> jenisBarang = "AC"
                R.id.rbJenisHp -> jenisBarang = "Handphone"
                R.id.rbJenisTelevisi -> jenisBarang = "Televisi"
                R.id.rbJenisLaptop -> jenisBarang = "Laptop"
            }
        }

        b.btnKirimOrder.setOnClickListener {
            if (!b.txNamaBarangOrder.text.toString().equals("") && !b.txKondisiBarangOrder.text.toString().equals("")) {
                AlertDialog.Builder(this)
                    .setTitle("Informasi!!")
                    .setIcon(R.drawable.warning)
                    .setMessage("Apakah Anda Ingin Mengirim Form Penjualan Barang?")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                        insert()
                    })
                    .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                        onBackPressed()
                        Toast.makeText(this,"Anda Membatalkan Pengiriman Form Servis", Toast.LENGTH_LONG).show()
                    })
                    .show()
            } else {
                Toast.makeText(this, "Tolong Masukkan Form dengan benar", Toast.LENGTH_LONG).show()
            }
        }

        fragmentOrderDetail = FragmentOrderDetail()
        b.bottomNavigationViewOrder.setOnNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.menuFormOrder -> {
                b.frameLayoutOrder.visibility = View.GONE
                b.judulMainOrder.setText("FORM ORDER    ")
            }
            R.id.menuDetailOrder -> {
                ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.frameLayoutOrder,fragmentOrderDetail).commit()
                b.frameLayoutOrder.setBackgroundColor(Color.argb(255,255,255,255))
                b.frameLayoutOrder.visibility = View.VISIBLE
                b.judulMainOrder.setText("DETAIL ORDER")
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        getProfil()
    }

    private fun getProfil() {
        var mode = "get_profil"
        var kd= preferences.getString("user", "").toString()
        uVM.getProfil(mode, kd).observe(this, androidx.lifecycle.Observer { user ->
            b.txNamaProfilOrder.setText(user.nama)
            b.txAlamatProfilOrder.setText(user.alamat)
        })
    }

    fun insert() {
        val data = OrderModel(
            preferences.getString("user", ""),
            b.txNamaBarangOrder.text.toString(),
            jenisBarang,
            b.txKondisiBarangOrder.text.toString()
        )

        tVM.insertOrder(data).observe(this, Observer { result ->
            var frag = FragmentOrderDetail()

            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutOrder, frag).commit()
            b.frameLayoutOrder.setBackgroundColor(Color.argb(255,255,255,255))
            b.frameLayoutOrder.visibility = View.VISIBLE
            b.judulMainOrder.setText("DETAIL ORDER")
            Toast.makeText(this, "Berhasil mengirim order barang!", Toast.LENGTH_SHORT).show()
            BaseApplication.notificationHelper.notifOrder(preferences.getString("nama", ""))
        })
    }
}