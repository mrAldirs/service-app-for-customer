package com.example.pelanggan_servis.View.Servis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.databinding.ServisRatingFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject

class ServisRatingFragment : BottomSheetDialogFragment() {
    private lateinit var binding: ServisRatingFragmentBinding
    lateinit var v: View
    lateinit var preferences: SharedPreferencesHelper
    lateinit var parent: ServisDetailActivity
    private var urlClass : UrlClass = UrlClass()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ServisRatingFragmentBinding.inflate(layoutInflater)
        v = binding.root
        parent = activity as ServisDetailActivity

        preferences = SharedPreferencesHelper(v.context)

        binding.btnBatalkan.setOnClickListener { dismiss() }
        binding.btnTutup.setOnClickListener { dismiss() }

        binding.btnSubmit.setOnClickListener {
            insert()
        }

        return v
    }

    fun insert() {
        val request = object : StringRequest(
            Method.POST, urlClass.urlMekanik,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    dismiss()
                    parent.detail(arguments?.get("kode").toString())
                    Toast.makeText(v.context, "Berhasil memberi rating!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(v.context, "Tidak dapat terhubung ke server", Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                val rating = binding.insRatingBar.rating.toDouble()
                hm.put("mode", "insert_rating")
                hm.put("kd_mekanik", arguments?.get("kode_mk").toString())
                hm.put("rating", rating.toString())
                hm.put("komen", binding.insKomentar.text.toString())
                hm.put("kd_user", preferences.getString("user", ""))

                return hm
            }
        }
        val queue = Volley.newRequestQueue(v.context)
        queue.add(request)
    }
}