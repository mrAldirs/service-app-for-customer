package com.example.pelanggan_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.GlobalData.BaseUrl
import com.example.pelanggan_servis.Model.UserModel
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun getProfil(mode: String, kd: String): LiveData<UserModel> {
        val data = MutableLiveData<UserModel>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlUser,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_user")
                val st2 = jsonObject.getString("nama")
                val st3 = jsonObject.getString("alamat")
                val st4 = jsonObject.getString("email")
                val st5 = jsonObject.getString("usia")
                val st6 = jsonObject.getString("no_hp")
                val st7 = jsonObject.getString("profil")

                val dataList = UserModel(st1, st2, st3, st4, st5, st6, st7)
                data.value = dataList
            },
            Response.ErrorListener { error ->
                // Error handling
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", mode)
                hm.put("kd_user", kd)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun edit(userModel: UserModel) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlUser,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String> {
                var namaFile =
                    "IMG_" + SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                        Date()
                    ) + ".jpg"

                val hm = HashMap<String, String>()
                hm.put("mode", "edit")
                hm.put("kd_user", userModel.kd_user)
                hm.put("nama", userModel.nama)
                hm.put("no_hp", userModel.no_hp)
                hm.put("usia", userModel.usia)
                hm.put("image", userModel.profil)
                hm.put("file", namaFile)

                return hm
            }
        }
        requestQueue.add(request)
        return result
    }
}