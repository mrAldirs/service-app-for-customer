package com.example.pelanggan_servis.View.Layout

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.View.Notifikasi.NotifikasiMainFragment
import com.example.pelanggan_servis.Pembayaran.PembayaranMainFragment
import com.example.pelanggan_servis.View.Profil.ProfilMainFragment
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.View.Riwayat.RiwayatMainFragment
import com.example.pelanggan_servis.View.Dialog.DialogLogin
import com.example.pelanggan_servis.View.Dialog.DialogLogin2
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.main_layout2.*
import kotlinx.android.synthetic.main.pin_fragment.tv
import org.json.JSONObject

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, IntentHelper {

    lateinit var preferences: SharedPreferencesHelper

    lateinit var ft : FragmentTransaction
    lateinit var urlClass : UrlClass
    var beli = 0
    var servis = 0
    var jual = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        urlClass = UrlClass()
        preferences = SharedPreferencesHelper(this)
        namaAkunMain.setText(preferences.getString("nama", "Guest"))

        val slide_start = AnimationUtils.loadAnimation(this, R.anim.translate_start)
        val slide_end = AnimationUtils.loadAnimation(this, R.anim.translate_end)
        val slide_top = AnimationUtils.loadAnimation(this, R.anim.translate_top)
        val slide_bottom = AnimationUtils.loadAnimation(this, R.anim.translate_bottom)
        val explade = AnimationUtils.loadAnimation(this, R.anim.explade)
        judulMain.startAnimation(slide_start)
        statusAkunMain.startAnimation(slide_top)
        namaAkunMain.startAnimation(slide_top)
        btnKatalogMain.startAnimation(slide_start)
        btnLokasiMain.startAnimation(slide_start)
        logoMain.startAnimation(slide_end)
        cardRiwayatMain.startAnimation(slide_end)
        btnServisPelanggan.startAnimation(slide_start)
        btnPesanPelanggan.startAnimation(explade)
        btnJualPelanggan.startAnimation(slide_end)
        judulMain2.startAnimation(explade)
        judulMain3.startAnimation(explade)
        imgInfoTerbaru.startAnimation(slide_bottom)
        iklanVV.startAnimation(slide_bottom)
        btnBeliPelanggan.startAnimation(explade)
        btnBeriMasukan.startAnimation(slide_end)
        btnMekanik.startAnimation(slide_start)

        if (namaAkunMain.text.equals("Guest")) {
            tvTotalSv.setText("0 Servis")
            tvTotalBeli.setText("O Beli")
            tvTotalJual.setText("O Jual")
        } else {
            totalTransaksi("total_servis")
            totalTransaksi2("total_jualorder")
            totalTransaksi3("total_beli")
        }

// button login
        if (namaAkunMain.text.equals("Guest")) {
            btnLoginMain.visibility = View.VISIBLE
        } else {
            btnLoginMain.visibility = View.GONE
        }

        btnLoginMain.setOnClickListener {
            start(loginMain())
        }
// end

// button Chat Admin (
        btnChatAdminMain.setOnClickListener {
            val kodeParent = preferences.getString("user", "")
            start(chatAdmin(kodeParent, "Chat"))
//            try {
//                val sendIntent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_TEXT, "Halo Admin")
//                    putExtra("jid", "${+6281249710599}@s.whatsapp.net")
//                    type = "text/plain"
//                    setPackage("com.whatsapp")
//                }
//                startActivity(sendIntent)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                val appPackageName = "com.whatsapp"
//                try {
//                    startActivity(
//                        Intent(
//                            Intent.ACTION_VIEW,
//                            Uri.parse("market://details?id=$appPackageName")
//                        )
//                    )
//                } catch (e: android.content.ActivityNotFoundException) {
//                    startActivity(
//                        Intent(
//                            Intent.ACTION_VIEW,
//                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
//                        )
//                    )
//                }
//            }
        }
// ) end

// button to see the location of the store
        btnLokasiMain.setOnClickListener {
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            startMapsActivity()
        }
// end

// button to view transaction details (
        btnRiwayatTransaksiMain.setOnClickListener {
            if (namaAkunMain.text.equals("Guest")) {
                var dialog = DialogLogin2()

                dialog.show(this.supportFragmentManager, "DialogLogin2")
            } else {
                var frag = FragmentMainDetail()

                val bundle = Bundle()
                bundle.putInt("ob", beli)
                bundle.putInt("s", servis)
                bundle.putInt("j", jual)
                frag.arguments = bundle

                frag.show(supportFragmentManager, "FragmentMainDetail")
            }
        }
// ) end

// button Katalog
        btnKatalogMain.setOnClickListener {
            start(katalog())
        }
// end

        btnMekanik.setOnClickListener {
            start(mekanik())
        }

// button servis
        btnServisPelanggan.setOnClickListener {
            if (namaAkunMain.text.equals("Guest")) {
                var dialog = DialogLogin2()

                dialog.show(this.supportFragmentManager, "DialogLogin2")
            } else {
                start(servis())
            }
        }
// end

// button order
        btnPesanPelanggan.setOnClickListener {
            if (namaAkunMain.text.equals("Guest")) {
                var dialog = DialogLogin2()

                dialog.show(this.supportFragmentManager, "DialogLogin2")
            } else {
                start(order())
            }
        }
// end

// button jual
        btnJualPelanggan.setOnClickListener {
            if (namaAkunMain.text.equals("Guest")) {
                var dialog = DialogLogin2()

                dialog.show(this.supportFragmentManager, "DialogLogin2")
            } else {
                start(jual())
            }
        }
// end

// button beli
        btnBeliPelanggan.setOnClickListener {
            if (namaAkunMain.text.equals("Guest")) {
                var dialog = DialogLogin2()

                dialog.show(this.supportFragmentManager, "DialogLogin2")
            } else {
                start(beli())
            }
        }
// end

// button beri masukan
        btnBeriMasukan.setOnClickListener {
            if (namaAkunMain.text.equals("Guest")) {
                var dialog = DialogLogin2()

                dialog.show(this.supportFragmentManager, "DialogLogin2")
            } else {
                start(feedback())
            }
        }
// end

        bottomNavigationView2.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.menuBeranda -> {
                frameLayout.visibility = View.GONE
            }
            R.id.menuRiwayat -> {
                if (namaAkunMain.text.equals("Guest")) {
                    val frag = DialogLogin()
                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.frameLayout,frag).commit()
                    frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
                    frameLayout.visibility = View.VISIBLE
                } else {
                    val frag = RiwayatMainFragment()
                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.frameLayout, frag).commit()
                    frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
                    frameLayout.visibility = View.VISIBLE
                    animateView(frameLayout)
                    true
                }
            }
            R.id.menuBayar -> {
                if (namaAkunMain.text.equals("Guest")) {
                    val frag = DialogLogin()
                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.frameLayout,frag).commit()
                    frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
                    frameLayout.visibility = View.VISIBLE
                } else {
                    val frag = PembayaranMainFragment()

                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.frameLayout, frag).commit()
                    frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
                    frameLayout.visibility = View.VISIBLE
                    animateView(frameLayout)
                    true
                }
            }
            R.id.menuNotif -> {
                if (namaAkunMain.text.equals("Guest")) {
                    val frag = DialogLogin()
                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.frameLayout,frag).commit()
                    frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
                    frameLayout.visibility = View.VISIBLE
                } else {
                    val frag = NotifikasiMainFragment()

                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.frameLayout,frag).commit()
                    frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
                    frameLayout.visibility = View.VISIBLE
                    animateView(frameLayout)
                    true
                }
            }
            R.id.menuAkun -> {
                if (namaAkunMain.text.equals("Guest")) {
                    val frag = DialogLogin()
                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.frameLayout,frag).commit()
                    frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
                    frameLayout.visibility = View.VISIBLE
                } else {
                    val frag = ProfilMainFragment()
                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.frameLayout, frag).commit()
                    frameLayout.setBackgroundColor(Color.argb(255, 255, 255, 255))
                    frameLayout.visibility = View.VISIBLE
                    animateView(frameLayout)
                    true
                }
            }
            else -> false
        }
        return true
    }

    fun animateView(view: View) {
        view.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(150)
            .withEndAction {
                view.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(150)
                    .start()
            }
            .start()
    }


    fun totalTransaksi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlRiwayat,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("jumlah")

                tvTotalSv.setText(st1+" Servis")
                servis = st1.toString().toInt()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode) {
                    "total_servis" -> {
                        hm.put("mode", "total_servis")
                        hm.put("kd_user", preferences.getString("user", ""))
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun totalTransaksi2(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlRiwayat,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("jumlah")

                tvTotalBeli.setText(st1+" Beli")
                beli = st1.toString().toInt()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode) {
                    "total_jualorder" -> {
                        hm.put("mode", "total_jualorder")
                        hm.put("kd_user", preferences.getString("user", ""))
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun totalTransaksi3(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlRiwayat,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("jumlah")

                tvTotalJual.setText(st1+" Jual")
                jual = st1.toString().toInt()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode) {
                    "total_beli" -> {
                        hm.put("mode", "total_beli")
                        hm.put("kd_user", preferences.getString("user", ""))
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}