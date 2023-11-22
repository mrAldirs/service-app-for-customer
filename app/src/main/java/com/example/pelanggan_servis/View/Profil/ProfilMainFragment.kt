package com.example.pelanggan_servis.View.Profil

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.View.Layout.MainActivity
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.View.Profil.PrivasiProfilActivity
import com.example.pelanggan_servis.databinding.ProfilMainFragmentBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class ProfilMainFragment : Fragment(), IntentHelper {
    private lateinit var b: ProfilMainFragmentBinding
    lateinit var v : View
    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = ProfilMainFragmentBinding.inflate(layoutInflater)
        v = b.root

        preferences = SharedPreferencesHelper(v.context)
        urlClass = UrlClass()

        b.btnKodeQrToko.setOnClickListener { QrProfilFragment().show(childFragmentManager, "QrCode") }
        b.profilMainStatusAkun.setOnClickListener { requireActivity().start(requireActivity().statusAkun()) }
        b.profilMainPengaturanAkun.setOnClickListener { requireActivity().start(requireActivity().pengaturanAkun()) }
        b.profilMainEmail.setOnClickListener { requireActivity().start(requireActivity().emailProfil(b.tvEmailProfilMain.text.toString())) }
        b.profilMainUsername.setOnClickListener { requireActivity().start(requireActivity().usernameProfil()) }
        b.btnKebijakanPrivasiProfilMain.setOnClickListener { requireActivity().start(requireActivity().privasiProfil()) }
        b.btnLayananProfilMain.setOnClickListener { requireActivity().start(requireActivity().layananProfil()) }
        b.btnPaymentProfilMain.setOnClickListener { requireActivity().start(requireActivity().paymentToko()) }
        b.btnProfilTokoProfilMain.setOnClickListener { requireActivity().start(requireActivity().profilToko()) }
        b.btnEditProfilMain.setOnClickListener { requireActivity().start(requireActivity().editProfil()) }
        b.btnUbahPinProfilMain.setOnClickListener { ProfilPinEditFragment().show(childFragmentManager, "") }

        b.btnKeluarProfilMain.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Logout")
                .setMessage("Apakah Anda ingin keluar aplikasi?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    val data = listOf("nama", "user", "username", "password", "pin")
                    preferences.remove(data)
                    requireActivity().start(requireActivity().logout())
                    requireActivity().finishAffinity()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                })
                .show()
            true
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        showProfil("show_profil")
    }

    fun showProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAkun,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_user")
                val st2 = jsonObject.getString("username")
                val st3 = jsonObject.getString("password")
                val st4 = jsonObject.getString("level")
                val st5 = jsonObject.getString("sts_akun")
                val st6 = jsonObject.getString("kd_user")
                val st7 = jsonObject.getString("nama")
                val st8 = jsonObject.getString("email")
                val st9 = jsonObject.getString("no_hp")
                val st10 = jsonObject.getString("profil")

                b.tvNamaPelangganProfilMain.setText(st7)
                b.tvNoHpPelangganProfilMain.setText(st9)
                Picasso.get().load(st10).into(b.imgProfilMain)
                b.tvStatusAkunProfilMain.setText(st5)
                b.tvEmailProfilMain.setText(st8)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
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
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}