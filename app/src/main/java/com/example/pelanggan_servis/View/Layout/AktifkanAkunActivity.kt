package com.example.pelanggan_servis.View.Layout

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.databinding.LoginAktifkanAkunActivityBinding
import com.google.android.recaptcha.Recaptcha
import com.google.android.recaptcha.RecaptchaAction
import com.google.android.recaptcha.RecaptchaClient
import kotlinx.coroutines.launch
import org.json.JSONObject

class AktifkanAkunActivity : AppCompatActivity(), IntentHelper {
    private lateinit var b: LoginAktifkanAkunActivityBinding
    private lateinit var recaptchaClient: RecaptchaClient
    lateinit var urlClass: UrlClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = LoginAktifkanAkunActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        urlClass = UrlClass()
        initializeRecaptchaClient()

        b.txPin1.setPinTextWatcher(b.txPin2)
        b.txPin2.setPinTextWatcher(b.txPin3)
        b.txPin3.setPinTextWatcher(b.txPin4)
        b.txPin4.setPinTextWatcher(b.txPin5)
        b.txPin5.setPinTextWatcher(b.txPin6)

        // Lakukan verifikasi CAPTCHA saat tombol submit diklik
        b.btnSubmit.setOnClickListener {
            executeLoginAction()
        }

    }

    fun EditText.setPinTextWatcher(nextEditText: EditText?) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    nextEditText?.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun initializeRecaptchaClient() {
        lifecycleScope.launch {
            Recaptcha.getClient(application, "6LdZCAYnAAAAAHdKJEReuQcAwa-IuNHvuXuHAtrF")
                .onSuccess { client ->
                    recaptchaClient = client
                }
                .onFailure { exception ->
                    // Handle communication errors ...
                    // See "Handle communication errors" section
                }
        }
    }

    private fun executeLoginAction() {
        lifecycleScope.launch {
            recaptchaClient
                .execute(RecaptchaAction.LOGIN)
                .onSuccess {
                    // CAPTCHA berhasil diverifikasi
                    // Lanjutkan dengan tindakan setelah verifikasi CAPTCHA
                    aktifkan()
                }
                .onFailure { exception ->
                    // Handle communication errors ...
                    // See "Handle communication errors" section
                }
        }
    }

    fun dialog() {
        AlertDialog.Builder(this)
            .setTitle("Peringatan!")
            .setIcon(R.drawable.warning)
            .setMessage("Silahkan Melakukan Login Kembali!")
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                noFlag(loginMain())
            })
            .show()
    }

    fun aktifkan() {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAkun,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil mengaktifkan akun lagi!", Toast.LENGTH_SHORT).show()
                    dialog()
                } else if (respon.equals("2")) {
                    Toast.makeText(this, "PIN yang Anda Masukkan salah!", Toast.LENGTH_SHORT).show()
                } else if (respon.equals("0")) {
                    Toast.makeText(this, "Username dan Password Anda salah!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                val pin = b.txPin1.text.toString() + b.txPin2.text.toString() + b.txPin3.text.toString() + b.txPin4.text.toString() + b.txPin5.text.toString() + b.txPin6.text.toString()

                hm.put("mode","aktifkan")
                hm.put("username", b.username.text.toString())
                hm.put("password", b.password.text.toString())
                hm.put("pin", pin)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}