package com.example.pelanggan_servis.Pembayaran

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.CurrencyHelper
import com.example.pelanggan_servis.Helper.MediaHelper
import com.example.pelanggan_servis.Helper.PhotoHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.databinding.PembayaranKonfirmasiFragmentBinding
import com.example.pelanggan_servis.databinding.PinFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.pembayaran_main_fragment.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FragmentPembayaranKonfirmasi : BottomSheetDialogFragment() {
    lateinit var b: PembayaranKonfirmasiFragmentBinding
    lateinit var v: View
    lateinit var thisParent: PembayaranMainFragment
    lateinit var preferences: SharedPreferencesHelper
    lateinit var urlClass : UrlClass
    lateinit var mediaHealper: MediaHelper
    lateinit var photoHelper: PhotoHelper
    var imStr = ""
    var namaFile = ""
    var fileUri = Uri.parse("")

    val pembayaranSp = arrayOf("--Pilih Metode Pembayaran--","COD","BCA","BRI","LinkAja","OVO","ShopeePay")
    lateinit var adapterPembayaran: ArrayAdapter<String>

    var jnsT = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = PembayaranKonfirmasiFragmentBinding.inflate(layoutInflater)
        v = b.root
        thisParent = parentFragment as PembayaranMainFragment

        preferences = SharedPreferencesHelper(v.context)

        val bundle = arguments
        b.bayarKonfirmasiKode.setText("Kode transaksi : "+bundle?.getString("kode").toString())
        val total = bundle?.getString("hrg").toString().toInt()
        b.totalBayar.setText(CurrencyHelper.formatCurrency(total))
        jnsT = bundle?.getString("jns").toString()

        urlClass = UrlClass()

        mediaHealper = MediaHelper(v.context)
        photoHelper = PhotoHelper()

        if (jnsT == "Order") {
            b.cardSpinner.visibility = View.VISIBLE
        } else {
            b.cardSpinner.visibility = View.GONE
        }

        adapterPembayaran = ArrayAdapter(v.context, android.R.layout.simple_list_item_1,pembayaranSp)
        b.bayarKonfirPayment.adapter = adapterPembayaran
        b.bayarKonfirPayment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent : AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    b.cardPayment.visibility = View.GONE
                    b.tvBuktiBayar.visibility = View.GONE
                    b.cardBuktiBayar.visibility = View.GONE
                } else if (position == 1) {
                    b.cardPayment.visibility = View.GONE
                    b.tvBuktiBayar.visibility = View.GONE
                    b.cardBuktiBayar.visibility = View.GONE
                } else {
                    b.tvBuktiBayar.visibility = View.VISIBLE
                    b.cardBuktiBayar.visibility = View.VISIBLE
                    b.cardPayment.visibility = View.VISIBLE
                }
            }
        }

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        b.btnChoosePhotoBayar.setOnClickListener {
            var photoMenu = PopupMenu(v.context, it)
            photoMenu.menuInflater.inflate(R.menu.menu_photo, photoMenu.menu)
            photoMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuPhotoRealtime -> {
                        requestPermission()
                    }
                    R.id.menuPhotoManager -> {
                        val intent = Intent()
                        intent.setType("image/*")
                        intent.setAction(Intent.ACTION_GET_CONTENT)
                        startActivityForResult(intent,mediaHealper.RcGallery())
                    }
                }
                false
            }
            photoMenu.show()
        }

        b.btnBatalkan.setOnClickListener { dismiss() }

        b.btnTutup.setOnClickListener { dismiss() }

        b.bayarKirimBayar.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda yakin ingin membeli barang ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    if (!imStr.equals("")) {
                        PinDialog().show(childFragmentManager, "PinFragment")
                    } else {
                        Toast.makeText(this.context,"Mohon kirim bukti pembayaran Anda!", Toast.LENGTH_LONG).show()
                    }
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

        return v
    }

    fun requestPermission() = runWithPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA ) {
        fileUri = photoHelper.getOutputMediaFileUri()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, photoHelper.getRcCamera())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            mediaHealper.RcGallery() -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        b.bayarBuktiImage.setBackgroundColor(Color.BLACK)
                        imStr = mediaHealper.getBitmapToString(data!!.data,b.bayarBuktiImage)
                        namaFile = "IMG_"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                            Date()
                        )+".jpg"
                    }
                    AppCompatActivity.RESULT_CANCELED -> {

                    }
                }
            }
            photoHelper.getRcCamera() -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        imStr = photoHelper.getBitMapToString(b.bayarBuktiImage, fileUri)
                        namaFile = photoHelper.getMyFileName()
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                        // kode untuk kondisi kedua jika dibatalkan
                    }
                }
            }
        }
    }

    private fun konfirmasi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlPembayaran,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                if (respon.equals("1")) {
                    dismiss()
                    thisParent.v.refresh.isRefreshing = true
                    thisParent.refreshData()
                    Toast.makeText(this.context,"Berhasil mengirim bukti pembayaran!", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "konfirmasi_pembayaran" -> {
                        hm.put("mode", "konfirmasi_pembayaran")
                        hm.put("kd_transaksi", thisParent.kdT)
                        hm.put("kd_user", preferences.getString("user", ""))
                        hm.put("image",imStr)
                        hm.put("file",namaFile)
                    }
                    "konfirmasi_pembayaran_order" -> {
                        hm.put("mode", "konfirmasi_pembayaran_order")
                        hm.put("kd_transaksi", thisParent.kdT)
                        hm.put("rek_payment", b.bayarKonfirPayment.selectedItem.toString())
                        hm.put("kd_user", preferences.getString("user", ""))
                        hm.put("image",imStr)
                        hm.put("file",namaFile)
                    }
                }
                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    class PinDialog : DialogFragment() {
        private lateinit var b: PinFragmentBinding
        lateinit var v: View
        lateinit var parent: FragmentPembayaranKonfirmasi
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            b = PinFragmentBinding.inflate(layoutInflater)
            v = b.root
            parent = parentFragment as FragmentPembayaranKonfirmasi

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
                val pin = b.txPin1.text.toString() + b.txPin2.text.toString() + b.txPin3.text.toString() + b.txPin4.text.toString() + b.txPin5.text.toString() + b.txPin6.text.toString()
                if (pin == parent.preferences.getString("pin", "")){
                    if (parent.jnsT.equals("Order")) {
                        parent.konfirmasi("konfirmasi_pembayaran_order")
                        dismiss()
                    } else {
                        parent.konfirmasi("konfirmasi_pembayaran")
                        dismiss()
                    }
                } else {
                    Toast.makeText(v.context, "Pin yang Anda masukkan salah!", Toast.LENGTH_SHORT).show()
                }
            }

            return v
        }
    }
}