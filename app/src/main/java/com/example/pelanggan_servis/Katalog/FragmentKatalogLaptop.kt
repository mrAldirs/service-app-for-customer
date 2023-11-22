package com.example.pelanggan_servis.Katalog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Adapter.AdapterKatalog
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import kotlinx.android.synthetic.main.katalog_jenis_fragment.view.btnSearchKatalog
import kotlinx.android.synthetic.main.katalog_jenis_fragment.view.rvKatalogJenis
import kotlinx.android.synthetic.main.katalog_jenis_fragment.view.textSearchKatalog
import org.json.JSONArray

class FragmentKatalogLaptop : Fragment() {
    lateinit var thisParent: KatalogMainActivity
    lateinit var v : View

    lateinit var urlClass: UrlClass

    val daftarBarang = mutableListOf<HashMap<String,String>>()
    lateinit var barangAdapter: AdapterKatalog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as KatalogMainActivity
        v = inflater.inflate(R.layout.katalog_jenis_fragment, container, false)

        urlClass = UrlClass()
        barangAdapter = AdapterKatalog(daftarBarang, thisParent)
        v.rvKatalogJenis.layoutManager = LinearLayoutManager(v.context)
        v.rvKatalogJenis.adapter = barangAdapter

        v.btnSearchKatalog.setOnClickListener {
            readBarang("read_laptop", v.textSearchKatalog.text.toString().trim())
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        readBarang("read_laptop", "")
    }

    private fun readBarang(mode: String, nama_barang: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlKatalog,
            Response.Listener { response ->
                daftarBarang.clear()
                if (response.equals(0)) {
                    Toast.makeText(this.context,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
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
                AlertDialog.Builder(v.context)
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
                    "read_laptop" -> {
                        hm.put("mode","read_laptop")
                        hm.put("nama_barang", nama_barang)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}