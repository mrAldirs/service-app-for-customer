package com.example.pelanggan_servis.View.Layout

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.Helper.MediaHelper
import com.example.pelanggan_servis.Helper.PhotoHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.View.Maps.MapsFragment
import com.example.pelanggan_servis.databinding.RegistrasiProfilActivityBinding
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale

class RegistrasiProfilActivity : AppCompatActivity(), IntentHelper {
    lateinit var binding: RegistrasiProfilActivityBinding
    lateinit var urlClass: UrlClass
    lateinit var mediaHealper: MediaHelper
    var imStr = ""
    lateinit var photoHelper: PhotoHelper
    var namaFile = ""
    var fileUri = Uri.parse("")
    var koordinat = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistrasiProfilActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        urlClass = UrlClass()
        mediaHealper = MediaHelper(this)
        photoHelper = PhotoHelper()

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.txPin1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    binding.txPin2.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.txPin2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    binding.txPin3.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.txPin3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    binding.txPin4.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.txPin4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    binding.txPin5.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.txPin5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    binding.txPin6.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.btnChoosePhotoRegis.setOnClickListener {
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

        binding.btnMaps.setOnClickListener {
            MapsFragment().show(supportFragmentManager, "")
        }

        binding.btnKirimRegis.setOnClickListener {
            if (binding.regisProfilUsia.text.toString().equals("") || imStr.equals("") || binding.regisProfilAlamat.text.toString().equals("")) {
                AlertDialog.Builder(this)
                    .setTitle("Perinagatan!!")
                    .setIcon(R.drawable.warning)
                    .setCancelable(false)
                    .setMessage("Masukkan data dengan benar, data tidak boleh kosong!")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                        null
                    })
                    .show()
            } else {
                AlertDialog.Builder(this)
                .setTitle("Perinagatan!!")
                .setIcon(R.drawable.warning)
                .setCancelable(false)
                .setMessage("Apakah Anda ingin mendaftarkan akun baru?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    registrasi("regis")
                    Toast.makeText(this, "Berhasil mendaftarkan Akun!", Toast.LENGTH_SHORT).show()
                    back(loginMain())
                })
                .show()
            }
        }
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
                        binding.regisProfilImage.setBackgroundColor(Color.BLACK)
                        imStr = mediaHealper.getBitmapToString(data!!.data,binding.regisProfilImage)
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
                        imStr = photoHelper.getBitMapToString(binding.regisProfilImage, fileUri)
                        namaFile = photoHelper.getMyFileName()
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                        // kode untuk kondisi kedua jika dibatalkan
                    }
                }
            }
        }
    }

    fun registrasi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.validasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "regis"->{
                        hm.put("mode","regis")
                        hm.put("username", intent?.getStringExtra("username").toString())
                        hm.put("password", intent?.getStringExtra("password").toString())
                        hm.put("pin", binding.txPin1.text.toString() + binding.txPin2.text.toString() + binding.txPin3.text.toString() + binding.txPin4.text.toString() + binding.txPin5.text.toString() + binding.txPin6.text.toString())
                        hm.put("nama", intent?.getStringExtra("nama").toString())
                        hm.put("alamat", binding.regisProfilAlamat.text.toString()+", "+binding.regisProfilDesa.text.toString()+", "+binding.regisProfilKecamatan.text.toString()+", "+binding.regisProfilKota.text.toString()+", "+binding.regisProfilProvinsi.text.toString())
                        hm.put("koordinat", koordinat)
                        hm.put("email", intent?.getStringExtra("email").toString())
                        hm.put("usia", binding.regisProfilUsia.text.toString())
                        hm.put("no_hp", intent?.getStringExtra("no_hp").toString())
                        hm.put("image",imStr)
                        hm.put("file",namaFile)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}