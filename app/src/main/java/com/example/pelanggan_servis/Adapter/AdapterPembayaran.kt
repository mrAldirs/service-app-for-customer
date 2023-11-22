package com.example.pelanggan_servis.Adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pelanggan_servis.Helper.CurrencyHelper
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.View.Pembayaran.PembayaranStrukActivity

class AdapterPembayaran(val dataBayar: List<HashMap<String, String>>) :
    RecyclerView.Adapter<AdapterPembayaran.HolderDataBayar>() {
    class HolderDataBayar(v: View) : RecyclerView.ViewHolder(v){
        val cd = v.findViewById<CardView>(R.id.cardPembayaran)
        val tot = v.findViewById<TextView>(R.id.adpTotalPembayaran)
        val tgl = v.findViewById<TextView>(R.id.adpTanggalPembayaran)
        val nmB = v.findViewById<TextView>(R.id.adpNamaBarangPembayaran)
        val sts = v.findViewById<TextView>(R.id.adpStatusPembayaran)
        val jns = v.findViewById<TextView>(R.id.adpJenisPembayaran)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataBayar {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_pembayaran, parent, false)
        return HolderDataBayar(v)
    }

    override fun getItemCount(): Int {
        return dataBayar.size
    }

    override fun onBindViewHolder(holder: HolderDataBayar, position: Int) {
        val data = dataBayar.get(position)

        holder.tot.text = CurrencyHelper.formatCurrency(data.get("total_bayar")!!.toInt())
        holder.nmB.setText(data.get("nama_barang"))

        val jns = data.get("jenis_transaksi").toString()
        if (jns.equals("Beli")) {
            holder.jns.setText("Jual")
        } else if (jns.equals("Jual")) {
            holder.jns.setText("Beli")
        } else {
            holder.jns.setText(data.get("jenis_transaksi"))
        }

        val status = data.get("status_pembayaran")
        if (status.toString().equals("Belum")) {
            holder.tgl.setText("Transaksi "+data.get("tgl_transaksi"))
            holder.sts.setText("belum bayar")
        } else if (status.toString().equals("Proses")) {
            holder.tgl.setText("Transaksi "+data.get("tgl_transaksi"))
            holder.sts.setText("proses")
            holder.sts.setTextColor(Color.parseColor("#5BCCFF"))
        } else {
            holder.tgl.setText(data.get("tgl_pembayaran"))
            holder.sts.setTextColor(Color.parseColor("#5AFF61"))
            holder.sts.setText(data.get("status_pembayaran"))
        }

        if (data.get("status_pembayaran").toString().equals("Selesai")) {
            holder.cd.setOnClickListener {
                val intent = Intent(it.context, PembayaranStrukActivity::class.java)
                intent.putExtra("kode", data.get("kd_transaksi").toString())
                it.context.startActivity(intent)
            }
        } else {
            null
        }
    }
}