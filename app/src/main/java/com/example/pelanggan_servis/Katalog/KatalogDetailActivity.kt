package com.example.pelanggan_servis.Katalog

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Beli.BeliBayarFragment
import com.example.pelanggan_servis.Helper.CurrencyHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.View.Layout.ImageDetailActivity
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.View.Dialog.DialogLogin2
import com.example.pelanggan_servis.databinding.KatalogDetailActivityBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class KatalogDetailActivity : AppCompatActivity() {
    private lateinit var b: KatalogDetailActivityBinding
    lateinit var urlClass : UrlClass
    lateinit var preferences: SharedPreferencesHelper
    var kdBrg = ""
    var imgUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = KatalogDetailActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        urlClass = UrlClass()

        var paket : Bundle? = intent.extras
        kdBrg = paket?.getString("kdB").toString()

        preferences = SharedPreferencesHelper(this)
        val validate = preferences.getString("nama", "Guest")

        b.detailKatalogBtnBeli.setOnClickListener {
            if (validate.equals("Guest")) {
                var dialog = DialogLogin2()

                dialog.show(this.supportFragmentManager, "")
            } else {
                var frag = BeliBayarFragment()

                val bundle = Bundle()
                bundle.putString("kode", kdBrg)
                frag.arguments = bundle

                frag.show(supportFragmentManager, "BeliBayarFragment")
            }
        }

        b.detailKatalogBtnBack.setOnClickListener { onBackPressed() }

        b.detailKatalogImage.setOnClickListener {
            val intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra("img", imgUrl)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        detailKatalog("detail_katalog")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun detailKatalog(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlKatalog,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("status_katalog")
                val st2 = jsonObject.getString("tgl_upload")
                val st3 = jsonObject.getString("nama_barang")
                val st4 = jsonObject.getString("jenis_barang")
                val st5 = jsonObject.getString("harga_katalog").toInt()
                val st6 = jsonObject.getString("spesifikasi")
                val st7 = jsonObject.getString("ket_barang")
                val st8 = jsonObject.getString("img_barang")
                val st9 = jsonObject.getString("kd_barang")

                kdBrg = st9
                b.detailKatalogTglUpload.setText(st2)
                b.detailKatalogNama.setText(st3)
                b.detailKatalogJenis.setText(st4)
                b.detailKatalogHarga.text = CurrencyHelper.formatCurrency(st5)
                b.detailKatalogSpesifikasi.setText(st6)

                if (st1.equals("Ready")) {
                    b.detailKatalogStatus.setText("Barang Tersedia")
                    b.detailKatalogStatus.setTextColor(Color.parseColor("#2046FF"))
                } else if (st1.equals("Pending")) {
                    b.detailKatalogStatus.setText("Transaksi Pending")
                    b.detailKatalogStatus.setTextColor(Color.parseColor("#EC0D0D"))
                } else {
                    b.detailKatalogStatus.setText("Barang Telah Terjual")
                    b.detailKatalogStatus.setTextColor(Color.parseColor("#17ad96"))
                }

                imgUrl = st8

                if (st7.toString().equals("null")) {
                    b.detailKatalogKeterangan.setText("*")
                } else {
                    b.detailKatalogKeterangan.setText("*"+ st7)
                }
                Picasso.get().load(st8).into(b.detailKatalogImage)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                var paket : Bundle? = intent.extras
                when(mode) {
                    "detail_katalog" -> {
                        hm.put("mode", "detail_katalog")
                        hm.put("kd_barang", paket?.getString("kdB").toString())
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}