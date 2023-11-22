package com.example.pelanggan_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.GlobalData.BaseUrl
import com.example.pelanggan_servis.Model.DetailTransaksiModel
import com.example.pelanggan_servis.Model.RiwayatModel
import com.example.pelanggan_servis.Model.TransaksiModel
import org.json.JSONArray
import org.json.JSONObject

class RiwayatRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun loadData(mode: String, kd_u: String): LiveData<List<RiwayatModel>> {
        val data = MutableLiveData<List<RiwayatModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlRiwayat,
            Response.Listener { response ->
                val dataList = mutableListOf<RiwayatModel>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val list = RiwayatModel(
                        jsonObject.getString("kd_transaksi"),
                        jsonObject.getString("nama"), "", "", "",
                        jsonObject.getString("nama_barang"), "",
                        jsonObject.getString("jenis_transaksi"),
                        jsonObject.getString("tgl_transaksi"), "",
                        jsonObject.getString("status_transaksi")
                    )
                    dataList.add(list)
                }
                data.value = dataList
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", mode)
                hm.put("kd_user", kd_u)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun detail(kd_t: String, kd_u: String): LiveData<RiwayatModel> {
        val data = MutableLiveData<RiwayatModel>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlRiwayat,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_transaksi")
                val st2 = jsonObject.getString("nama")
                val st3 = jsonObject.getString("alamat")
                val st4 = jsonObject.getString("email")
                val st5 = jsonObject.getString("no_hp")
                val st6 = jsonObject.getString("nama_barang")
                val st7 = jsonObject.getString("jenis_barang")
                val st8 = jsonObject.getString("jenis_transaksi")
                val st9 = jsonObject.getString("tgl_transaksi")
                val st12 = jsonObject.getString("img_barang")
                val st13 = jsonObject.getString("status_transaksi")

                val dataList = RiwayatModel(st1,st2,st3,st4,st5,st6,st7,st8,st9,st12,st13)
                data.value = dataList
            },
            Response.ErrorListener { error ->
                // Error handling
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "detail")
                hm.put("kd_transaksi", kd_t)
                hm.put("kd_user", kd_u)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }


    fun detailTransaksi(kd: String) : LiveData<DetailTransaksiModel> {
        val data = MutableLiveData<DetailTransaksiModel>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlRiwayat,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama_barang")
                val st2 = jsonObject.getString("jenis_barang")
                val st3 = jsonObject.getString("img_barang")
                val st4 = jsonObject.getString("kd_transaksi")
                val st5 = jsonObject.getString("jenis_transaksi")
                val st6 = jsonObject.getString("status_pembayaran")
                val st7 = jsonObject.getString("catatan_transaksi")
                val st8 = jsonObject.getString("bayar")
                val st9 = jsonObject.getString("biaya_admin")
                val st10 = jsonObject.getString("ongkir")
                val st11 = jsonObject.getString("total_bayar")

                val dataList = DetailTransaksiModel(st1,st2, st3, st4, st5, st6, st7, st8, st9, st10, st11)
                data.value = dataList
            },
            Response.ErrorListener { error ->
                // Error handling
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "detail_transaksi")
                hm.put("kd_transaksi", kd)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun batalkan(kd:String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlJualBeli,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "batalkan")
                hm.put("kd_transaksi", kd)
                return hm
            }
        }
        requestQueue.add(request)
        return result
    }
}