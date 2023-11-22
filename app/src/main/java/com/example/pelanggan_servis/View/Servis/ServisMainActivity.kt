package com.example.pelanggan_servis.View.Servis

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pelanggan_servis.Helper.BaseApplication
import com.example.pelanggan_servis.Helper.MediaHelper
import com.example.pelanggan_servis.Helper.PhotoHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.View.Layout.MainActivity
import com.example.pelanggan_servis.Model.ServisModel
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.ViewModel.ServisViewModel
import com.example.pelanggan_servis.ViewModel.UserViewModel
import com.example.pelanggan_servis.databinding.ServisMainActivityBinding
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import java.text.SimpleDateFormat
import java.util.*

class ServisMainActivity : AppCompatActivity() {
    private lateinit var b: ServisMainActivityBinding
    private lateinit var sVM : ServisViewModel
    private lateinit var uVM : UserViewModel

    lateinit var preferences: SharedPreferencesHelper
    lateinit var mediaHealper: MediaHelper
    var imStr = ""
    lateinit var photoHelper: PhotoHelper
    var namaFile = ""
    var fileUri = Uri.parse("")

    var kd_user = ""
    var jenisServis = ""

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ServisMainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        uVM = ViewModelProvider(this).get(UserViewModel::class.java)
        sVM = ViewModelProvider(this).get(ServisViewModel::class.java)

        preferences = SharedPreferencesHelper(this)
        mediaHealper = MediaHelper(this)
        photoHelper = PhotoHelper()

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        b.rgJenisServis.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rbJenisAC -> jenisServis = "AC"
                R.id.rbJenisHp -> jenisServis = "Handphone"
                R.id.rbJenisTelevisi -> jenisServis = "Televisi"
                R.id.rbJenisLaptop -> jenisServis = "Laptop"
            }
        }

        b.btnChoosePhotoServis.setOnClickListener {
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

        b.btnKirimServisPelanggan.setOnClickListener {
            if (!b.txMerkServis.text.toString().equals("") && !b.txKontakServis.text.toString().equals("") && !b.txKerusakanServis.text.toString().equals("")) {
                if (!imStr.equals("")) {
                    AlertDialog.Builder(this)
                        .setTitle("Informasi!!")
                        .setIcon(R.drawable.warning)
                        .setMessage("Apakah Anda Ingin Mengirim Form Servis?")
                        .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                            insert(namaFile)
                        })
                        .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                            startActivity(Intent(this, MainActivity::class.java))
                            Toast.makeText(this,"Anda Membatalkan Pengiriman Form Servis", Toast.LENGTH_LONG).show()
                        })
                        .show()
                } else {
                    Toast.makeText(this, "Tolong masukkan foto terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
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
                        b.imgPhotoServis.setBackgroundColor(Color.BLACK)
                        imStr = mediaHealper.getBitmapToString(data!!.data,b.imgPhotoServis)
                        namaFile = "IMG_"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                            Date()
                        )+".jpg"
                    }
                    RESULT_CANCELED -> {

                    }
                }
            }
            photoHelper.getRcCamera() -> {
                when (resultCode) {
                    RESULT_OK -> {
                        imStr = photoHelper.getBitMapToString(b.imgPhotoServis, fileUri)
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
            b.txNamaServis.setText(user.nama)
            b.txAlamatServis.setText(user.alamat)
            b.txKontakServis.setText(user.no_hp)
        })
    }

    fun insert(nm: String) {
        val data = ServisModel(
            kd_user = preferences.getString("user", ""),
            nama_barang = b.txMerkServis.text.toString(),
            jenis_barang = jenisServis,
            kerusakan = b.txKerusakanServis.text.toString(),
            ket_barang = b.txKetBarangServis.text.toString(),
            image = imStr
        )

        sVM.insert(data, nm).observe(this, androidx.lifecycle.Observer { success ->
            Toast.makeText(this, "Berhasil mengirim data servis!", Toast.LENGTH_SHORT).show()
            onBackPressed()
            BaseApplication.notificationHelper.notifServis(preferences.getString("nama", ""))
        })
    }
}