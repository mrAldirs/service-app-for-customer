package com.example.pelanggan_servis.View.Jual

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pelanggan_servis.Helper.BaseApplication
import com.example.pelanggan_servis.Helper.MediaHelper
import com.example.pelanggan_servis.Helper.PhotoHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.Helper.SpinnerHelper
import com.example.pelanggan_servis.Model.JualModel
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.ViewModel.TransaksiViewModel
import com.example.pelanggan_servis.ViewModel.UserViewModel
import com.example.pelanggan_servis.databinding.JualMainActivityBinding
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.yigitserin.currencyedittext.CurrencyEditText
import java.text.SimpleDateFormat
import java.util.*

class JualMainActivity : AppCompatActivity() {

    private lateinit var b: JualMainActivityBinding
    private lateinit var uVM : UserViewModel
    private lateinit var tVM : TransaksiViewModel
    lateinit var preferences: SharedPreferencesHelper
    lateinit var mediaHealper: MediaHelper
    lateinit var photoHelper: PhotoHelper
    var imStr = ""
    var namaFile = ""
    var fileUri = Uri.parse("")
    val spHelper: SpinnerHelper = SpinnerHelper(this)

    var jenisBarang = ""
    var bHarga = ""

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = JualMainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        uVM = ViewModelProvider(this).get(UserViewModel::class.java)
        tVM = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        preferences = SharedPreferencesHelper(this)
        mediaHealper = MediaHelper(this)
        photoHelper = PhotoHelper()

        val harga : CurrencyEditText = findViewById(R.id.txHargaBarangJual)
        harga.locale = Locale("id", "ID")
        harga.decimalDigits = 2

        spHelper.pembayaran(b.spPayment)
        b.spPayment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent : AdapterView<*>?) {
                b.insNorek.visibility = View.GONE
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if (selectedItem.equals("COD")) {
                    b.insNorek.visibility = View.GONE
                } else {
                    b.insNorek.visibility = View.VISIBLE
                }
            }
        }

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        b.rgJenisBarangJual.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rbJenisAC -> jenisBarang = "AC"
                R.id.rbJenisHp -> jenisBarang = "Handphone"
                R.id.rbJenisTelevisi -> jenisBarang = "Televisi"
                R.id.rbJenisLaptop -> jenisBarang = "Laptop"
            }
        }

        b.btnChoosePhotoBarangJual.setOnClickListener {
            var photoMenu = PopupMenu(this, it)
            photoMenu.menuInflater.inflate(R.menu.menu_photo, photoMenu.menu)
            photoMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuPhotoRealtime -> {
                        requestPermission()
                    }
                    R.id.menuPhotoManager -> {
                        val intent = Intent()
                        intent.setType("image/*")
                        intent.setAction(Intent.ACTION_GET_CONTENT)
                        startActivityForResult(intent,mediaHealper.RcGallery())
                    }
                }
                false
            }
            photoMenu.show()
        }

        b.btnKirimJualBarang.setOnClickListener {
            if (!b.txNamaBarangJual.text.toString().equals("") && !b.txKondisiBarangJual.text.toString().equals("")) {
                AlertDialog.Builder(this)
                    .setTitle("Informasi!!")
                    .setIcon(R.drawable.warning)
                    .setMessage("Apakah Anda Ingin Mengirim Form Penjualan Barang?")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                        insert(namaFile)
                        onBackPressed()
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
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun requestPermission() = runWithPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA ) {
        fileUri = photoHelper.getOutputMediaFileUri()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, photoHelper.getRcCamera())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            mediaHealper.RcGallery() -> {
                when (resultCode) {
                    RESULT_OK -> {
                        b.imgPhotoBarangJual.setBackgroundColor(Color.BLACK)
                        imStr = mediaHealper.getBitmapToString(data!!.data,b.imgPhotoBarangJual)
                        namaFile = "IMG_"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())+".jpg"
                    }
                    RESULT_CANCELED -> {

                    }
                }
            }
            photoHelper.getRcCamera() -> {
                when (resultCode) {
                    RESULT_OK -> {
                        imStr = photoHelper.getBitMapToString(b.imgPhotoBarangJual, fileUri)
                        namaFile = photoHelper.getMyFileName()
                    }
                    RESULT_CANCELED -> {
                        // kode untuk kondisi kedua jika dibatalkan
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getProfil()
    }

    private fun getProfil() {
        var mode = "get_profil"
        var kd= preferences.getString("user", "").toString()
        uVM.getProfil(mode, kd).observe(this, androidx.lifecycle.Observer { user ->
            b.txNamaProfilJual.setText(user.nama)
            b.txAlamatProfilJual.setText(user.alamat)
        })
    }

    fun insert(nm: String) {
        bHarga = b.txHargaBarangJual.text.toString().replace(",", "")
        val data = JualModel(
            preferences.getString("user", ""),
            b.txNamaBarangJual.text.toString(),
            jenisBarang,
            imStr,
            b.txKondisiBarangJual.text.toString(),
            spHelper.getSelectedItem(b.spPayment) + " " + b.txNoRek.text.toString(),
            bHarga
        )

        tVM.insertJual(data, nm).observe(this, androidx.lifecycle.Observer {
            Toast.makeText(this, "Berhasil mengirim form penjualan barang!", Toast.LENGTH_SHORT).show()
            onBackPressed()
            BaseApplication.notificationHelper.notifJual(preferences.getString("nama", ""))
        })
    }
}