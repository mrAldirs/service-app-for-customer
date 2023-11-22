package com.example.pelanggan_servis.Adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pelanggan_servis.Model.RiwayatModel
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.View.Riwayat.RiwayatDetailActivity
import com.example.pelanggan_servis.View.Servis.ServisDetailActivity

class AdapterRiwayatSelesai (private var dataList: List<RiwayatModel>)
    : RecyclerView.Adapter<AdapterRiwayatSelesai.HolderDataTransaksi>() {
    class HolderDataTransaksi(v: View) : RecyclerView.ViewHolder(v) {
        val namaTrans = v.findViewById<TextView>(R.id.adpNamaPelangganTransaksi)
        val tanggal = v.findViewById<TextView>(R.id.adpTanggalTransaksi)
        val namaBg = v.findViewById<TextView>(R.id.adpNamaBarangTransaksi)
        val jenis = v.findViewById<TextView>(R.id.adpJenisTransaksi)
        val status = v.findViewById<TextView>(R.id.adpStatusTransaksi)
        val card = v.findViewById<CardView>(R.id.cardTransaksi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataTransaksi {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_transaksi, parent, false)
        return HolderDataTransaksi(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataTransaksi, position: Int) {
        val data = dataList.get(position)

        holder.namaTrans.setText(data.nama)
        holder.tanggal.setText(data.tgl_transaksi)
        holder.namaBg.setText(data.nama_barang)

        val jns = data.jenis_transaksi
        if (jns.equals("Beli")) {
            holder.jenis.setText("Jual")
        } else if (jns.equals("Jual")) {
            holder.jenis.setText("Beli")
        } else {
            holder.jenis.setText(data.jenis_transaksi)
        }

        if (jns.equals("Servis")) {
            holder.card.setOnClickListener {
                val intent = Intent(it.context, ServisDetailActivity::class.java)
                intent.putExtra("kode", data.kd_transaksi)
                it.context.startActivity(intent)
            }
        } else {
            holder.card.setOnClickListener {
                val intent = Intent(it.context, RiwayatDetailActivity::class.java)
                intent.putExtra("kdT", data.kd_transaksi)
                it.context.startActivity(intent)
            }
        }

        val status = data.status_transaksi
        if (status.equals("Belum")) {
            holder.status.setText("belum dikonfirmasi!")
            holder.status.setTextColor(Color.RED)
        } else if (status.equals("Proses")) {
            holder.status.setText("proses")
        } else {
            holder.status.visibility = View.GONE
        }
    }

    fun setData(newDataList: List<RiwayatModel>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}