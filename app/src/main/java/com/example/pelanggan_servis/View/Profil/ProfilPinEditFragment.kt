package com.example.pelanggan_servis.View.Profil

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.databinding.PinFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject

class ProfilPinEditFragment : BottomSheetDialogFragment(), IntentHelper {
    private lateinit var b: PinFragmentBinding
    lateinit var v: View
    lateinit var parent: ProfilMainFragment
    lateinit var preferences: SharedPreferencesHelper
    lateinit var urlClass: UrlClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = PinFragmentBinding.inflate(layoutInflater)
        v = b.root
        parent = parentFragment as ProfilMainFragment

        urlClass = UrlClass()
        preferences = SharedPreferencesHelper(v.context)
        b.tvPin.setText("Masukkan PIN baru")

        b.txPin1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    b.txPin2.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        b.txPin2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    b.txPin3.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        b.txPin3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    b.txPin4.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        b.txPin4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    b.txPin5.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        b.txPin5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    b.txPin6.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        b.btnKirimPin.setOnClickListener {
            val request = object : StringRequest(
                Method.POST,parent.urlClass.urlUser,
                Response.Listener { response ->
                    val jsonObject = JSONObject(response)
                    val respon = jsonObject.getString("respon")

                    if (respon.equals("1")) {
                        Toast.makeText(v.context, "Berhasil mengubah PIN transaksi!", Toast.LENGTH_SHORT).show()
                        dismiss()
                        dialog()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(v.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): MutableMap<String, String>? {
                    val hm = HashMap<String, String>()
                    val pin = b.txPin1.text.toString() + b.txPin2.text.toString() + b.txPin3.text.toString() + b.txPin4.text.toString() + b.txPin5.text.toString() + b.txPin6.text.toString()
                    hm.put("mode","edit_pin")
                    hm.put("kd_user", parent.preferences.getString("user", ""))
                    hm.put("pin", pin)

                    return hm
                }
            }
            val queue = Volley.newRequestQueue(v.context)
            queue.add(request)
        }

        return v
    }

    fun dialog() {
        AlertDialog.Builder(v.context)
            .setTitle("Peringatan!")
            .setIcon(R.drawable.warning)
            .setMessage("Silahkan Melakukan Login Kembali!")
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                val data = listOf("nama", "user", "username", "password", "pin")
                preferences.remove(data)
                parent.requireActivity().start(parent.requireActivity().loginMain())
                parent.requireActivity().finishAffinity()
            })
            .show()
    }
}