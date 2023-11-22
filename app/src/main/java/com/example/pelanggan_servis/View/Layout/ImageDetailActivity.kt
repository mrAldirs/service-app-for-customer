package com.example.pelanggan_servis.View.Layout

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.databinding.ImageDetailBinding

class ImageDetailActivity : AppCompatActivity() {
    private lateinit var b: ImageDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ImageDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        var paket : Bundle? = intent.extras
        var url = paket?.getString("img").toString()

        b.imageDetail.webViewClient = WebViewClient()
        b.imageDetail.loadUrl(url)
        b.imageDetail.settings.javaScriptEnabled = true
        b.imageDetail.settings.loadWithOverviewMode = true
        b.imageDetail.settings.useWideViewPort = true

        b.imageDetail.setOnLongClickListener { v ->
            val hitTestResult = b.imageDetail.hitTestResult
            if (hitTestResult.type == WebView.HitTestResult.IMAGE_TYPE ||
                hitTestResult.type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                AlertDialog.Builder(this)
                    .setTitle("Simpan Gambar")
                    .setMessage("Anda ingin menyimpan gambar ini?")
                    .setPositiveButton("Simpan") { _, _ ->
                        val imgUrl = hitTestResult.extra
                        imgUrl?.let { downloadImage(it) }
                        Toast.makeText(this, "Berhasil menyimpan Gambar!", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
                true
            } else false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun downloadImage(imageUrl: String) {
        val request = DownloadManager.Request(Uri.parse(imageUrl))
            .setTitle("Gambar")
            .setDescription("Mengunduh Gambar")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}.jpg")
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}