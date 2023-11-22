package com.example.pelanggan_servis.View.Order

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.PhotoHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.databinding.OrderKonfirmasiFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import org.json.JSONObject

class FragmentOrderKonfirmasi : BottomSheetDialogFragment() {
    private lateinit var b: OrderKonfirmasiFragmentBinding
    lateinit var preferences: SharedPreferencesHelper
    lateinit var thisParent: OrderMainActivity
    lateinit var v: View
    lateinit var urlClass: UrlClass
    val pembayaranSp = arrayOf("--Pilih Metode Pembayaran--","COD","BCA","BRI","LinkAja","OVO","ShopeePay")
    lateinit var adapterPembayaran: ArrayAdapter<String>
    lateinit var photoHelper: PhotoHelper
    var namaFile = ""
    var imStr = ""
    var fileUri = Uri.parse("")
    var kdT = ""
    var kdP = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = OrderKonfirmasiFragmentBinding.inflate(layoutInflater)
        v = b.root
        thisParent = activity as OrderMainActivity

        preferences = SharedPreferencesHelper(v.context)
        urlClass = UrlClass()

        val bundle = arguments
        kdT = bundle?.getString("kode").toString()
        b.bayarOrderKodeTransaksi.setText("Kode "+bundle?.getString("kode").toString())

        photoHelper = PhotoHelper()

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        adapterPembayaran = ArrayAdapter(v.context, android.R.layout.simple_list_item_1,pembayaranSp)
        b.bayarOrderPayment.adapter = adapterPembayaran
        b.bayarOrderPayment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        b.btnChoosePhotoOrder.setOnClickListener {
            requestPermission()
        }

        b.btnBatalkan.setOnClickListener {dismiss()}

        b.btnTutup.setOnClickListener {dismiss()}

        b.btnKirimKBayarOrder.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda yakin ingin melakukan pembayaran!")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    konfirmasiOrder("bayar_order")
                    Toast.makeText(this.context,"Sukses melakukan pembayaran transaksi "+kdT, Toast.LENGTH_LONG).show()
                    dismiss()
                    thisParent.onBackPressed()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

        b.btnKonfirmasi.setOnClickListener {
            b.tvKonfirmasi.visibility = View.VISIBLE
            b.cardSpinner.visibility = View.VISIBLE
            b.tvBuktiBayar.visibility = View.VISIBLE
            b.cardBuktiBayar.visibility = View.VISIBLE
            b.btnKonfirmasi.visibility = View.GONE
            b.btnKirimKBayarOrder.visibility = View.VISIBLE
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
            photoHelper.getRcCamera() -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        imStr = photoHelper.getBitMapToString(b.bayarOrderImage, fileUri)
                        namaFile = photoHelper.getMyFileName()
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                        // kode untuk kondisi kedua jika dibatalkan
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        detailPembayaran("detail_pembayaran_order")
    }

    private fun detailPembayaran(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlJualBeli,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_pembayaran")
                val st2 = jsonObject.getString("bayar")
                val st3 = jsonObject.getString("biaya_admin")
                val st4 = jsonObject.getString("ongkir")
                val st5 = jsonObject.getString("total_bayar")

                kdP = st1
                b.bayarOrderHargaBarang.setText("Rp."+st2)
                b.bayarOrderAdmin.setText("Rp."+st3)
                b.bayarOrderOngkir.setText("Rp."+st4)
                b.bayarOrderTotalPembayaran.setText("Rp."+st5)

            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                val bundle = arguments
                when(mode) {
                    "detail_pembayaran_order" -> {
                        hm.put("mode", "detail_pembayaran_order")
                        hm.put("kd_transaksi", bundle?.getString("kode").toString())
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    fun konfirmasiOrder(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlJualBeli,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)

            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "bayar_order" -> {
                        hm.put("mode", "bayar_order")
                        hm.put("kd_pembayaran", kdP)
                        hm.put("kd_transaksi", kdT)
                        hm.put("kd_user", preferences.getString("user", ""))
                        hm.put("rek_payment", b.bayarOrderPayment.selectedItem.toString())
                        hm.put("image", imStr)
                        hm.put("file", namaFile)
                    }
                }
                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}