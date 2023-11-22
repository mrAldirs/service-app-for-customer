package com.example.pelanggan_servis.View.Pembayaran

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.pelanggan_servis.databinding.PembayaranStrukActivityBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PembayaranStrukActivity : AppCompatActivity() {
    private lateinit var b: PembayaranStrukActivityBinding
    private val REQUEST_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = PembayaranStrukActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val webSettings: WebSettings = b.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setSupportZoom(true)

        var paket : Bundle? = intent.extras
        var id = paket?.getString("kode").toString()
        b.webView.loadUrl("http://192.168.40.77/api_service/struk_pembayaran.php?id=$id")

        b.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

                b.btnDownload.setOnClickListener {
                    captureWebViewAsJpg()
                }
            }
        }
    }

    private fun captureWebViewAsJpg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_PERMISSION)
                return
            }
        }

        val bitmap = Bitmap.createBitmap(b.webView.width, b.webView.height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        b.webView.draw(canvas)

        val file = File(getExternalFilesDir(null), "bukti_bayar.jpg")
        try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream.flush()
            }
            openDownloadedFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        b.webView.invalidate()
    }

    private fun openDownloadedFile(file: File) {
        val contentUri = FileProvider.getUriForFile(
            applicationContext,
            "${applicationContext.packageName}.fileprovider",
            file
        )
        val downloadIntent = Intent(Intent.ACTION_VIEW)
        downloadIntent.setDataAndType(contentUri, "image/jpeg")
        downloadIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(downloadIntent)
    }
}