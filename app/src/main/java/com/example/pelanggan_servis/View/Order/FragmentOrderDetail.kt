package com.example.pelanggan_servis.View.Order

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.ViewModel.TransaksiViewModel
import com.example.pelanggan_servis.databinding.OrderDetailFragmentBinding
import com.squareup.picasso.Picasso

class FragmentOrderDetail : Fragment(), IntentHelper {
    private lateinit var b: OrderDetailFragmentBinding
    private lateinit var tVM : TransaksiViewModel
    lateinit var thisParent : OrderMainActivity
    lateinit var v : View
    lateinit var preferences: SharedPreferencesHelper

    var kdT = ""
    var kdB = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = OrderDetailFragmentBinding.inflate(layoutInflater)
        v = b.root
        thisParent = activity as OrderMainActivity
        preferences = SharedPreferencesHelper(v.context)

        tVM = ViewModelProvider(this).get(TransaksiViewModel::class.java)

        detailOrder()

        b.btnBatalkanOrder.setOnClickListener {
            AlertDialog.Builder(thisParent)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda membatalkan orderan ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    delete(kdB, kdT)
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                })
                .show()
            true
        }

        b.btnKonfirmasiBayarOrder.setOnClickListener {
            var frag = FragmentOrderKonfirmasi()
            var paket : Bundle? = thisParent.intent.extras
            var kode = paket?.getString("kode")

            val bundle = Bundle()
            bundle.putString("kode", kode)
            bundle.putString("kode", kdT)
            frag.arguments = bundle

            frag.show(childFragmentManager, "FragmementorderKonfirmasi")
        }
        return v
    }

    fun detailOrder() {
        tVM.getDetailOrder("show_detail_order", preferences.getString("user", ""))
            .observe(requireActivity(), Observer { (order, error) ->
                if (order == null) {
                    b.notfound.visibility = View.VISIBLE
                    b.btnBatalkanOrder.visibility = View.GONE
                    b.btnKonfirmasiBayarOrder.visibility = View.GONE
                    b.nestedScrollView2.visibility = View.GONE
                } else {
                    kdT = order.kd_transaksi
                    kdB = order.kd_barang
                    b.detailOrderId.setText(order.kd_transaksi)
                    b.detailOrderNama.setText(order.nama)
                    b.detailOrderTglTransaksi.setText(order.tgl_transaksi)
                    b.detailOrderAlamat.setText(order.alamat)
                    b.detailOrderEmail.setText(order.email)
                    b.detailOrderNoHp.setText(order.no_hp)
                    b.detailOrderNamaBarang.setText(order.nama_barang)
                    b.detailOrderJenisBarang.setText(order.jenis_barang)
                    b.detailOrderKeterangan.setText(order.ket_barang)
                    b.detailOrderCatatan.setText(order.catatan_transaksi)
                    Picasso.get().load(order.img_barang).into(b.detailOrderImage)

                    b.btnChat.setOnClickListener { it : View ->
                        val like = (it.context as AppCompatActivity)
                        like.start(like.chatAdmin(order.kd_transaksi, "Pre-order"))
                    }

                    if (order.status_transaksi.equals("Belum")) {
                        b.detailOrderStatus.setText("Belum dikonfirmasi admin")
                        b.detailOrderStatus.setTextColor(Color.RED)
                        b.btnKonfirmasiBayarOrder.visibility = View.GONE
                    } else {
                        b.detailOrderStatus.setTextColor(Color.BLUE)
                        b.detailOrderStatus.setText("selesai")
                        b.btnKonfirmasiBayarOrder.visibility = View.GONE
                    }

                    if (order.catatan_transaksi.equals("null")) {
                        b.cardKonfirm.visibility = View.GONE
                        b.detailOrderImage.visibility = View.GONE
                    } else {
                        b.cardKonfirm.visibility = View.VISIBLE
                        b.detailOrderImage.visibility = View.VISIBLE
                    }
                }
            })
    }

    fun delete(kdB: String, kdT: String) {
        tVM.deleteOrder(kdB, kdT).observe(requireActivity(), Observer { result ->
            Toast.makeText(v.context,"Berhasil membatalkan order!", Toast.LENGTH_LONG).show()
            thisParent.onBackPressed()
        })
    }
}