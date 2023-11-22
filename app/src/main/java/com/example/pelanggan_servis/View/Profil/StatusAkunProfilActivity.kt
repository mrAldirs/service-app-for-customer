package com.example.pelanggan_servis.View.Profil

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.databinding.ProfilStatusakunAtivityBinding
import org.json.JSONObject

class StatusAkunProfilActivity : AppCompatActivity(), IntentHelper {
    private lateinit var b: ProfilStatusakunAtivityBinding
    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ProfilStatusakunAtivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        preferences = SharedPreferencesHelper(this)
        urlClass = UrlClass()

        b.btnClose.setOnClickListener {
            onBackPressed()
        }

        b.btnNonAktifkanAkun.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Peringatan!!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda Ingin menonaktifkan akun Anda?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    nonaktif("nonaktif")
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }
    }

    private fun nonaktif(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAkun,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                if(respon.equals("1")){
                    Toast.makeText(this,"Berhasil Menonaktifkan Akun Anda", Toast.LENGTH_LONG).show()
                    val data = listOf("nama", "user", "username", "password")
                    preferences.remove(data)
                    start(logout())
                    finishAffinity()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                when(mode) {
                    "nonaktif" -> {
                        hm.put("mode", "nonaktif")
                        hm.put("kd_user", preferences.getString("user", ""))
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}