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
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.databinding.ProfilEmailActivityBinding
import com.example.pelanggan_servis.databinding.ProfilEmailEditFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject

class EmailProfilActivity : AppCompatActivity() {
    private lateinit var b: ProfilEmailActivityBinding
    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ProfilEmailActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        preferences = SharedPreferencesHelper(this)
        urlClass = UrlClass()

        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        b.btnUbahEmail.setOnClickListener {
            EditEmail().show(supportFragmentManager, "")
        }

        var paket : Bundle? = intent.extras
        b.emailEmail.setText(paket?.getString("em").toString())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    class EditEmail : BottomSheetDialogFragment() {
        private lateinit var hb: ProfilEmailEditFragmentBinding
        lateinit var v: View
        lateinit var parent: EmailProfilActivity

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            hb = ProfilEmailEditFragmentBinding.inflate(layoutInflater)
            v = hb.root
            parent = activity as EmailProfilActivity

            hb.btnBatalkan.setOnClickListener { dismiss() }
            hb.btnTutup.setOnClickListener { dismiss() }

            hb.btnEdit.setOnClickListener {
                AlertDialog.Builder(v.context)
                    .setTitle("Peringatan!")
                    .setIcon(R.drawable.warning)
                    .setMessage("Apakah Anda ingin mengedit email aplikasi Anda?")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                        val isEmailValid = isValidEmail(hb.email.text.toString())
                        if (isEmailValid) {
                            edit()
                        } else {
                            Toast.makeText(v.context, "Format Email tidak Valid!", Toast.LENGTH_SHORT).show()
                        }
                    })
                    .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show()
            }

            return v
        }

        fun isValidEmail(email: String): Boolean {
            val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
            val pattern = Regex(emailRegex)
            return pattern.matches(email)
        }

        fun edit() {
            val request = object : StringRequest(
                Method.POST,parent.urlClass.urlUser,
                Response.Listener { response ->
                    val jsonObject = JSONObject(response)
                    val respon = jsonObject.getString("respon")

                    if (respon.equals("1")) {
                        Toast.makeText(v.context, "Berhasil mengubah email aplikasi!", Toast.LENGTH_SHORT).show()
                        dismiss()
                        parent.recreate()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(v.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): MutableMap<String, String>? {
                    val hm = HashMap<String, String>()
                    hm.put("mode","edit_email")
                    hm.put("kd_user", parent.preferences.getString("user", ""))
                    hm.put("email", hb.email.text.toString())

                    return hm
                }
            }
            val queue = Volley.newRequestQueue(v.context)
            queue.add(request)
        }
    }
}