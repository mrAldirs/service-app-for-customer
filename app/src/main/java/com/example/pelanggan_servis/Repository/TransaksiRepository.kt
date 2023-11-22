package com.example.pelanggan_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.GlobalData.BaseUrl
import com.example.pelanggan_servis.Model.JualModel
import com.example.pelanggan_servis.Model.OrderModel
import com.example.pelanggan_servis.Model.RiwayatModel
import com.example.pelanggan_servis.Model.TransaksiModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TransaksiRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun insertJual(jual: JualModel, nmFile: String): LiveData<Boolean> {
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
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                hm.put("mode", "insert_jual")
                hm.put("kd_user", jual.kd_user)
                hm.put("nama_barang", jual.nama_barang)
                hm.put("jenis_barang", jual.jenis_barang)
                hm.put("ket_barang", jual.ket_barang)
                hm.put("rek_payment", jual.rek_payment)
                hm.put("total_bayar", jual.total_bayar)
                hm.put("image", jual.image)
                hm.put("file", nmFile)

                return hm
            }
        }

        requestQueue.add(request)
        return result
    }

    fun insertOrder(order: OrderModel): LiveData<Boolean> {
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
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                hm.put("mode", "insert_order")
                hm.put("kd_user", order.kd_user)
                hm.put("nama_barang", order.nama_barang)
                hm.put("jenis_barang", order.jenis_barang)
                hm.put("ket_barang", order.ket_barang)

                return hm
            }
        }

        requestQueue.add(request)
        return result
    }

    fun getDetailOrder(mode: String, kd: String, callback: (TransaksiModel?, String?) -> Unit): (TransaksiModel?, String?) -> Unit {
        val request = object : StringRequest(
            Method.POST, baseUrl.urlJualBeli,
            Response.Listener { response ->
                try {
                    if (response == "0") {
                        callback(null, "No data found")
                    } else {
                        val jsonArray = JSONArray(response)
                        val detailOrderList = mutableListOf<TransaksiModel>()
                        for (x in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(x)
                            val detailOrder = TransaksiModel(
                                jsonObject.getString("kd_user"),
                                jsonObject.getString("nama"),
                                jsonObject.getString("alamat"),
                                jsonObject.getString("email"),
                                jsonObject.getString("no_hp"),
                                jsonObject.getString("kd_barang"),
                                jsonObject.getString("nama_barang"),
                                jsonObject.getString("jenis_barang"),
                                jsonObject.getString("image"),
                                jsonObject.getString("ket_barang"),
                                jsonObject.getString("kd_transaksi"), "",
                                jsonObject.getString("tgl_transaksi"),
                                jsonObject.getString("status_transaksi"),
                                jsonObject.getString("catatan_transaksi")
                            )
                            detailOrderList.add(detailOrder)
                        }
                        callback(detailOrderList[0], null)
                    }
                } catch (e: JSONException) {
                    callback(null, "Error parsing JSON")
                }
            },
            Response.ErrorListener { error ->
                callback(null, error.message)
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params.put("mode", mode)
                params.put("kd_user", kd)
                return params
            }
        }

        requestQueue.add(request)
        return callback
    }


    fun deleteOrder(kdB: String, kdT:String): LiveData<Boolean> {
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
                hm.put("mode", "delete_order")
                hm.put("kd_transaksi", kdT)
                hm.put("kd_barang", kdB)
                return hm
            }
        }
        requestQueue.add(request)
        return result
    }
}