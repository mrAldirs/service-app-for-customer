package com.example.pelanggan_servis.Beli

import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Adapter.AdapterBeli
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.databinding.BeliMainActivityBinding
import org.json.JSONArray

class BeliMainActivity : AppCompatActivity() {
    private lateinit var b: BeliMainActivityBinding
    lateinit var urlClass: UrlClass

    val daftarBarang = mutableListOf<HashMap<String,String>>()
    lateinit var barangAdapter: AdapterBeli

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = BeliMainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        urlClass = UrlClass()
        barangAdapter = AdapterBeli(daftarBarang, this)
        b.rvBeli.layoutManager = GridLayoutManager(this, 2)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing) // Mengambil nilai spacing dari dimens.xml

        val includeEdge = true // Atur true jika Anda ingin padding pada tepi luar grid
        val itemDecoration = GridSpacingItemDecoration(2, spacingInPixels, includeEdge)
        b.rvBeli.addItemDecoration(itemDecoration)
        b.rvBeli.adapter = barangAdapter

        b.btnSearchKatalog.setOnClickListener {
            readBarang("read_all", b.textSearchKatalog.text.toString().trim())
        }

        readBarang("read_all", "")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun readBarang(mode: String, nama_barang: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlJualBeli,
            Response.Listener { response ->
                daftarBarang.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("nama_barang",jsonObject.getString("nama_barang"))
                        frm.put("tgl_upload",jsonObject.getString("tgl_upload"))
                        frm.put("jenis_barang",jsonObject.getString("jenis_barang"))
                        frm.put("status_katalog",jsonObject.getString("status_katalog"))
                        frm.put("img_barang",jsonObject.getString("img_barang"))
                        frm.put("harga_katalog",jsonObject.getString("harga_katalog"))
                        frm.put("kd_barang",jsonObject.getString("kd_barang"))
                        frm.put("kd_katalog",jsonObject.getString("kd_katalog"))

                        daftarBarang.add(frm)
                    }
                    barangAdapter.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                AlertDialog.Builder(this)
                    .setTitle("Peringatan!")
                    .setIcon(R.drawable.warning)
                    .setMessage("Koneksi Eror tidak dapat terhubung ke database! Pastikan Anda memiliki jaringan Internet")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "read_all" -> {
                        hm.put("mode","show_data_beli")
                        hm.put("nama_barang", nama_barang)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // Mendapatkan posisi item
            val column = position % spanCount // Mendapatkan kolom saat ini

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 2) * spacing / spanCount

                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 2) * spacing / spanCount

                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }
}