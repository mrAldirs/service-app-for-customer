package com.example.pelanggan_servis.View.Layout

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.GlobalData.BaseUrl
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.databinding.LoginFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.login_activity.etUsername
import kotlinx.android.synthetic.main.login_activity.frameLayoutLogin
import org.json.JSONObject

class LoginFragment : BottomSheetDialogFragment(), IntentHelper {
    private lateinit var b: LoginFragmentBinding
    lateinit var preferences: SharedPreferencesHelper
    lateinit var thisParent : LoginActivity
    lateinit var v : View
    val baseUrl: BaseUrl = BaseUrl

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = LoginFragmentBinding.inflate(layoutInflater)
        v = b.root
        thisParent = activity as LoginActivity

        preferences = SharedPreferencesHelper(v.context)

        b.btnBatalkan.setOnClickListener { dismiss() }

        b.btnTutup.setOnClickListener { dismiss() }

        b.btnKirimLogin.setOnClickListener {
            thisParent.b.progressBar.visibility = View.VISIBLE
            validationAccount("login")
        }

        return v
    }

    fun validationAccount(mode: String){
        val request = object : StringRequest(
            Method.POST,baseUrl.validasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val level = jsonObject.getString("level")
                val statusAkun = jsonObject.getString("sts_akun")
                val pin = jsonObject.getString("pin")
                if(level.equals("Pelanggan") && statusAkun.equals("AKTIF")){
                    val nama = jsonObject.getString("nama")
                    val user = jsonObject.getString("kd_user")
                    val username = jsonObject.getString("username")
                    val password = jsonObject.getString("password")
                    val data = mapOf(
                        "nama" to nama,
                        "user" to user,
                        "username" to username,
                        "password" to password,
                        "pin" to pin
                    )
                    preferences.saveString(data)

                    context?.let { requireActivity().start(it.login()) }
                    requireActivity().finishAffinity()
                } else if(statusAkun.equals("NON")){
                    AlertDialog.Builder(thisParent)
                        .setIcon(R.drawable.warning)
                        .setTitle("Peringatan!")
                        .setMessage("Status Akun Anda Telah Dinon-aktifkan, Apakah Anda ingin mengaktifkan?")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                            thisParent.b.progressBar.visibility = View.GONE
                            requireActivity().start(requireActivity().aktifkan())
                        })
                        .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                            thisParent.b.progressBar.visibility = View.GONE
                        })
                        .show()
                }else{
                    AlertDialog.Builder(thisParent)
                        .setIcon(R.drawable.warning)
                        .setTitle("Peringatan!")
                        .setMessage("Username atau Katasandi Anda salah!")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                            thisParent.b.progressBar.visibility = View.GONE
                        })
                        .show()
                }
            },
            Response.ErrorListener { error ->
                androidx.appcompat.app.AlertDialog.Builder(thisParent)
                    .setTitle("Peringatan!")
                    .setIcon(R.drawable.warning)
                    .setMessage("Koneksi Eror tidak dapat terhubung ke database! Pastikan Anda memiliki jaringan Internet")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                        thisParent.b.progressBar.visibility = View.GONE
                    })
                    .show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("username",thisParent.etUsername.text.toString())
                hm.put("password",b.etPassword.text.toString())
                when(mode) {
                    "login" -> {
                        hm.put("mode", "login")
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(thisParent)
        queue.add(request)
    }
}