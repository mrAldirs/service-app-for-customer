package com.example.pelanggan_servis.View.Profil

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.databinding.ProfilPengaturanActivityBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class PengaturanProfilActivity : AppCompatActivity() {
    private lateinit var b: ProfilPengaturanActivityBinding
    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ProfilPengaturanActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        preferences = SharedPreferencesHelper(this)
        urlClass = UrlClass()

        b.btnClose.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onStart() {
        super.onStart()
        showProfil("show_profil")
    }

    private fun showProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAkun,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_user")
                val st2 = jsonObject.getString("username")
                val st3 = jsonObject.getString("password")
                val st4 = jsonObject.getString("level")
                val st5 = jsonObject.getString("usia")
                val st6 = jsonObject.getString("kd_user")
                val st7 = jsonObject.getString("nama")
                val st8 = jsonObject.getString("email")
                val st9 = jsonObject.getString("no_hp")
                val st10 = jsonObject.getString("profil")
                val st11 = jsonObject.getString("alamat")
                val st12 = jsonObject.getString("alamat")
                val st13 = jsonObject.getString("alamat")

                b.pengaturanNama.setText(st7)
                b.pengaturanAlamat.setText(st11+", "+st12+", "+st13)
                b.pengaturanUsia.setText(st5+" Tahun")
                b.pengaturanNoHp.setText(st9)
                b.pengaturanEmail.setText(st8)
                Picasso.get().load(st10).into(b.pengaturanImage)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode) {
                    "show_profil" -> {
                        hm.put("mode", "show_profil")
                        hm.put("kd_user", preferences.getString("user", ""))
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}