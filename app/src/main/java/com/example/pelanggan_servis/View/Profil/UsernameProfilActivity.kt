package com.example.pelanggan_servis.View.Profil

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.databinding.ProfilUsernameActivityBinding
import com.example.pelanggan_servis.databinding.ProfilUsernameEditFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject

class UsernameProfilActivity : AppCompatActivity(), IntentHelper {
    private lateinit var b: ProfilUsernameActivityBinding
    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ProfilUsernameActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        preferences = SharedPreferencesHelper(this)
        urlClass = UrlClass()

        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        b.btnUbahUsername.setOnClickListener {
            EditUsername().show(supportFragmentManager, "")
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

    fun dialog() {
        AlertDialog.Builder(this)
            .setTitle("Peringatan!")
            .setIcon(R.drawable.warning)
            .setMessage("Silahkan Melakukan Login Kembali!")
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                val data = listOf("nama", "user", "username", "password", "pin")
                preferences.remove(data)
                start(loginMain())
                finishAffinity()
            })
            .show()
    }

    private fun showProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAkun,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_user")
                val st2 = jsonObject.getString("username")
                val st3 = jsonObject.getString("password")

                b.usernameProfilUsername.setText(st2)
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

    class EditUsername : BottomSheetDialogFragment() {
        private lateinit var hb: ProfilUsernameEditFragmentBinding
        lateinit var v: View
        lateinit var parent: UsernameProfilActivity

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            hb = ProfilUsernameEditFragmentBinding.inflate(layoutInflater)
            v = hb.root
            parent = activity as UsernameProfilActivity

            hb.btnBatalkan.setOnClickListener { dismiss() }
            hb.btnTutup.setOnClickListener { dismiss() }

            hb.btnEdit.setOnClickListener {
                AlertDialog.Builder(v.context)
                    .setTitle("Peringatan!")
                    .setIcon(R.drawable.warning)
                    .setMessage("Apakah Anda ingin mengedit username password Anda?")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                        edit()
                    })
                    .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show()
            }

            return v
        }

        fun edit() {
            val request = object : StringRequest(
                Method.POST,parent.urlClass.urlUser,
                Response.Listener { response ->
                    val jsonObject = JSONObject(response)
                    val respon = jsonObject.getString("respon")

                    if (respon.equals("1")) {
                        Toast.makeText(v.context, "Berhasil mengubah username dan password!", Toast.LENGTH_SHORT).show()
                        dismiss()
                        parent.dialog()
                    } else if (respon.equals("2")) {
                        Toast.makeText(v.context, "Password lama yang diinputkan salah!", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(v.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): MutableMap<String, String>? {
                    val hm = HashMap<String, String>()
                    hm.put("mode","edit_username")
                    hm.put("kd_user", parent.preferences.getString("user", ""))
                    hm.put("username", hb.username.text.toString())
                    hm.put("password", hb.passwordBaru.text.toString())
                    hm.put("passwordLama", hb.passwordLama.text.toString())

                    return hm
                }
            }
            val queue = Volley.newRequestQueue(v.context)
            queue.add(request)
        }
    }
}