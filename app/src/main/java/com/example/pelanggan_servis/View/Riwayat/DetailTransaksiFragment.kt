package com.example.pelanggan_servis.View.Riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pelanggan_servis.Helper.CurrencyHelper
import com.example.pelanggan_servis.ViewModel.RiwayatViewModel
import com.example.pelanggan_servis.databinding.RiwayatDetailTransaksiFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.riwayat_detail_transaksi_fragment.detail_admin

class DetailTransaksiFragment : BottomSheetDialogFragment() {
    private lateinit var b: RiwayatDetailTransaksiFragmentBinding
    lateinit var v: View
    private lateinit var rVM: RiwayatViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = RiwayatDetailTransaksiFragmentBinding.inflate(layoutInflater)
        v = b.root

        rVM = ViewModelProvider(this).get(RiwayatViewModel::class.java)

        b.btnBatalkan.setOnClickListener { dismiss() }
        b.btnTutup.setOnClickListener { dismiss() }

        return v
    }

    override fun onStart() {
        super.onStart()
        showDetail()
    }

    private fun showDetail() {
        val kd = arguments?.getString("kode").toString()
        rVM.detailTransaksi(kd).observe(requireActivity(), Observer { detail ->
            b.detailBarang.setText(detail.nama_barang)
            b.detailJenisBarang.setText(detail.jenis_barang)
            Picasso.get().load(detail.img_barang).into(b.detailImage)
            b.detailStatusBayar.setText(detail.status_bayar)
            b.detailTotal.text = CurrencyHelper.formatCurrency(detail.total_bayar.toInt())
            b.detailCatatan.setText(detail.catatan_transaksi)
            b.detailKode.setText(detail.kd_transaksi)

            if (detail.jenis_transaksi.equals("Beli")) {
                b.detailJenisTransaksi.setText("Jual")
            } else if (detail.jenis_transaksi.equals("Jual")) {
                b.detailJenisTransaksi.setText("Beli")
            } else {
                b.detailJenisTransaksi.setText(detail.jenis_transaksi)
            }

            if (detail.ongkir.equals("null")) {
                b.detailOngkir.setText("0")
            } else {
                b.detailOngkir.text = CurrencyHelper.formatCurrency(detail.ongkir.toInt())
            }

            if (detail.biaya_admin.equals("null")) {
                b.detailAdmin.setText("0")
            } else {
                b.detailAdmin.text = CurrencyHelper.formatCurrency(detail.biaya_admin.toInt())
            }

            if (detail.bayar.equals("null")) {
                b.detailHarga.setText("0")
            } else {
                b.detailHarga.text = CurrencyHelper.formatCurrency(detail.bayar.toInt())
            }
        })
    }
}