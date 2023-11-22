package com.example.pelanggan_servis.Repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.GlobalData.BaseUrl
import com.example.pelanggan_servis.Model.ServisDetailModel
import com.example.pelanggan_servis.Model.ServisModel
import org.json.JSONException
import org.json.JSONObject

class ServisRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun insert(servis: ServisModel, nmFile: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlServis,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                hm.put("mode", "insert")
                hm.put("kd_user", servis.kd_user)
                hm.put("nama_barang", servis.nama_barang)
                hm.put("jenis_barang", servis.jenis_barang)
                hm.put("kerusakan", servis.kerusakan)
                hm.put("ket_barang", servis.ket_barang)
                hm.put("image", servis.image)
                hm.put("file", nmFile)

                return hm
            }
        }

        requestQueue.add(request)
        return result
    }

    fun detail(kd: String): LiveData<ServisDetailModel> {
        val data = MutableLiveData<ServisDetailModel>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlServis,
            Response.Listener { response ->
                Log.d("JSON_RESPONSE", response)

                try {
                    val jsonObject = JSONObject(response)
                    val respon = jsonObject.getString("respon")
                    val st1 = jsonObject.getString("nama_barang")
                    val st2 = jsonObject.getString("jenis_barang")
                    val st3 = jsonObject.getString("img_barang")
                    val st8 = jsonObject.getString("kd_transaksi")
                    val st9 = jsonObject.getString("tgl_transaksi")
                    val st10 = jsonObject.getString("jenis_transaksi")
                    val st11 = jsonObject.getString("status_transaksi")
                    val st12 = jsonObject.getString("catatan_transaksi")
                    val st13 = jsonObject.getString("tgl_servis")
                    val st14 = jsonObject.getString("kerusakan")

                    if (respon.equals("1")) {
                        val st4 = jsonObject.getString("kd_mekanik")
                        val st5 = jsonObject.getString("nama_mekanik")
                        val st6 = jsonObject.getString("rating").toFloat()
                        val st7 = jsonObject.getString("img_mekanik")

                        val dataList = ServisDetailModel(
                            st1,st2,st3,st4,st5,st6.toString(),st7,st8,st9,st10,st11,st12, st13,st14
                        )

                        data.value = dataList
                    } else if (respon.equals("2")) {
                        val dataList = ServisDetailModel(
                            st1,st2,st3,"","","","",st8,st9,st10,st11,st12, st13,st14
                        )

                        data.value = dataList
                    }
                } catch (e: JSONException) {
                    // Handle JSON parsing error
                }
            },
            Response.ErrorListener { error ->
                // Error handling
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "detail")
                hm.put("kd_transaksi", kd)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }
}