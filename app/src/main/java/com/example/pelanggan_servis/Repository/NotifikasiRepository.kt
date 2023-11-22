package com.example.pelanggan_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.GlobalData.BaseUrl
import com.example.pelanggan_servis.Model.NotifikasiModel
import org.json.JSONArray
import org.json.JSONObject

class NotifikasiRepository (context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun loadDataPesan(kd: String): LiveData<List<NotifikasiModel>> {
        val data = MutableLiveData<List<NotifikasiModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlNotifikasi,
            Response.Listener { response ->
                val dataList = mutableListOf<NotifikasiModel>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val list = NotifikasiModel(
                        kd_notif = jsonObject.getString("kd_notif"),
                        nama = jsonObject.getString("nama"),
                        teks_notif = jsonObject.getString("teks_notif"),
                        jamtanggal_notif = jsonObject.getString("jamtanggal_notif"),
                        jenis_notif = jsonObject.getString("jenis_notif")
                    )
                    dataList.add(list)
                }
                data.value = dataList
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "read_notifikasi_pesan")
                hm.put("kd_user", kd)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun loadDataInformasi(): LiveData<List<NotifikasiModel>> {
        val data = MutableLiveData<List<NotifikasiModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlNotifikasi,
            Response.Listener { response ->
                val dataList = mutableListOf<NotifikasiModel>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val list = NotifikasiModel(
                        kd_notif = jsonObject.getString("kd_notif"),
                        "",
                        teks_notif = jsonObject.getString("teks_notif"),
                        jamtanggal_notif = jsonObject.getString("jamtanggal_notif"),
                        jenis_notif = jsonObject.getString("jenis_notif")
                    )
                    dataList.add(list)
                }
                data.value = dataList
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "read_notifikasi_informasi")

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun delete(kd: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlNotifikasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "delete")
                hm.put("kd_notif", kd)
                return hm
            }
        }
        requestQueue.add(request)
        return result
    }
}