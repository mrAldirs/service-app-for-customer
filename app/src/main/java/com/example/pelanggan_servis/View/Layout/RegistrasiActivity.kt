package com.example.pelanggan_servis.View.Layout

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.GlobalData.BaseUrl
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.databinding.RegistrasiActivityBinding
import org.json.JSONObject
import java.util.HashMap

class RegistrasiActivity : AppCompatActivity(), IntentHelper {
    lateinit var b: RegistrasiActivityBinding
    val baseUrl : BaseUrl = BaseUrl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = RegistrasiActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        b.btnLoginRegis.setOnClickListener {
            back(loginMain())
        }

        b.btnCekAkun.setOnClickListener {
            if (b.regisPassword.text.toString() == b.regisKonfirmPassword.text.toString()) {
                if (b.regisNama.text.toString().equals("") || b.regisEmail.text.toString().equals("") || b.regisNoHp.text.toString().equals("") || b.regisUsername.text.toString().equals("") || b.regisPassword.text.toString().equals("")) {
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
                    cekAkun("cek_akun")
                }
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Perinagatan!!")
                    .setIcon(R.drawable.warning)
                    .setCancelable(false)
                    .setMessage("Password yang anda masukkan salah, konfirmasi Password dengan benar!")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                        null
                    })
                    .show()
            }
        }
    }

    private fun cekAkun(mode: String) {
        val request = object : StringRequest(
            Method.POST,baseUrl.validasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("0")) {
                    Toast.makeText(this, "Username telah digunakan!", Toast.LENGTH_SHORT).show()
                } else if (respon.equals("1")) {
                    Toast.makeText(this, "Nomor Handphone telah terdaftar!", Toast.LENGTH_SHORT).show()
                } else if (respon.equals("2")) {
                    Toast.makeText(this, "Email Anda telah terdaftar!", Toast.LENGTH_SHORT).show()
                } else if (respon.equals("3")) {
                    Toast.makeText(this, "Nama Lengkap Anda telah terdaftar!", Toast.LENGTH_SHORT).show()
                } else if (respon.equals("4")) {
                    val intent = Intent(this@RegistrasiActivity, RegistrasiProfilActivity::class.java)
                    intent.putExtra("username", b.regisUsername.text.toString())
                    intent.putExtra("password", b.regisPassword.text.toString())
                    intent.putExtra("nama", b.regisNama.text.toString())
                    intent.putExtra("email", b.regisEmail.text.toString())
                    intent.putExtra("no_hp", b.regisNoHp.text.toString())
                    startActivity(intent)
                    Toast.makeText(this, "Akun belum terdaftar, lengkapi profil jika ingin mendaftarkan akun!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "cek_akun"->{
                        hm.put("mode","cek_akun")
                        hm.put("username", b.regisUsername.text.toString())
                        hm.put("nohp", b.regisNoHp.text.toString())
                        hm.put("nama", b.regisNama.text.toString())
                        hm.put("email", b.regisEmail.text.toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    override fun onBackPressed() {
        if (b.frameLayoutRegis.visibility == View.VISIBLE) {
            AlertDialog.Builder(this)
                .setTitle("Perinagatan!!")
                .setIcon(R.drawable.warning)
                .setCancelable(false)
                .setMessage("Tolong lengkapi profil Anda terlebih dahulu, agar akun dapat didaftarkan")
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                    null
                })
                .show()
        } else {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}