package com.example.pelanggan_servis.View.Profil

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pelanggan_servis.Helper.MediaHelper
import com.example.pelanggan_servis.Helper.PhotoHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.Model.UserModel
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.View.Layout.MainActivity
import com.example.pelanggan_servis.ViewModel.UserViewModel
import com.example.pelanggan_servis.databinding.ProfilEditActivityBinding
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfilEditActivity : AppCompatActivity() {
    private lateinit var b: ProfilEditActivityBinding
    private lateinit var uVM : UserViewModel
    private lateinit var preferences: SharedPreferencesHelper

    lateinit var mediaHealper: MediaHelper
    var imStr = ""
    lateinit var photoHelper: PhotoHelper
    var namaFile = ""
    var fileUri = Uri.parse("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ProfilEditActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        preferences = SharedPreferencesHelper(this)
        uVM = ViewModelProvider(this).get(UserViewModel::class.java)

        mediaHealper = MediaHelper(this)
        photoHelper = PhotoHelper()

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        b.btnChoose.setOnClickListener {
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

        b.btnSimpan.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Peringatan!!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin mengedit profil Anda?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    edit()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                    null
                })
                .show()
        }

        profil()
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
                    AppCompatActivity.RESULT_OK -> {
                        b.imageProfil.setBackgroundColor(Color.BLACK)
                        imStr = mediaHealper.getBitmapToString(data!!.data,b.imageProfil)
                        namaFile = "IMG_"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                            Date()
                        )+".jpg"
                    }
                    AppCompatActivity.RESULT_CANCELED -> {

                    }
                }
            }
            photoHelper.getRcCamera() -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        imStr = photoHelper.getBitMapToString(b.imageProfil, fileUri)
                        namaFile = photoHelper.getMyFileName()
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                        // kode untuk kondisi kedua jika dibatalkan
                    }
                }
            }
        }
    }

    private fun profil() {
        uVM.getProfil("get_profil", preferences.getString("user", ""))
            .observe(this, Observer { profil ->
                b.profilNama.setText(profil.nama)
                b.profilNohp.setText(profil.no_hp)
                b.profilUsia.setText(profil.usia)
                Picasso.get().load(profil.profil).into(b.imageProfil)
            })
    }

    private fun edit() {
        val data = UserModel(
            preferences.getString("user", ""),b.profilNama.text.toString(),"","",b.profilUsia.text.toString(),b.profilNohp.text.toString(), imStr
        )

        uVM.edit(data).observe(this, Observer { result ->
            onBackPressed()
            Toast.makeText(this, "Berhasil mengubah data profil!", Toast.LENGTH_SHORT).show()
        })
    }
}