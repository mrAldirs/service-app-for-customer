package com.example.pelanggan_servis.View.Profil

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.ViewModel.UserViewModel
import com.example.pelanggan_servis.databinding.ProfilTokoActivityBinding
import com.squareup.picasso.Picasso

class TokoProfilActivity : AppCompatActivity() {
    private lateinit var b: ProfilTokoActivityBinding
    private lateinit var pVM : UserViewModel
    private val locationPermissionCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ProfilTokoActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        pVM = ViewModelProvider(this).get(UserViewModel::class.java)
        pVM.getProfil("get_profil", "USR000001").observe(this, Observer { profil ->
            b.profilNama.text = profil.nama
            b.profilNohp.text = profil.no_hp
            Picasso.get().load(profil.profil).into(b.profilImage)
            b.profilEmail.text = profil.email
            b.profilAlamat.text = profil.alamat
        })

        val webSettings: WebSettings = b.webView.settings
        webSettings.javaScriptEnabled = true
        b.webView.webViewClient = WebViewClient()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationPermissionCode
                )
            } else {
                enableLocation()
            }
        } else {
            enableLocation()
        }

        b.webView.loadUrl("https://www.google.com/maps/place/Rukin+Servis+TV/@-7.9096488,112.1270951,16.89z/data=!4m15!1m8!3m7!1s0x2e78f661707e125d:0x1dcad186c48efdd2!2sJajar,+Kec.+Wates,+Kabupaten+Kediri,+Jawa+Timur!3b1!8m2!3d-7.9046877!4d112.1236468!16s%2Fg%2F121d1srj!3m5!1s0x2e78f78d380489ef:0xe2b60dfb3e98f112!8m2!3d-7.9102667!4d112.1278103!16s%2Fg%2F11hyj19fsx?entry=ttu")
    }

    private fun enableLocation() {
        b.webView.settings.setGeolocationEnabled(true)
        b.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        b.webView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                callback.invoke(origin, true, false)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableLocation()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}