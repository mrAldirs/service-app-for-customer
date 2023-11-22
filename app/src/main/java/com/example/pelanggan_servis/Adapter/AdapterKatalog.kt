package com.example.pelanggan_servis.Adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pelanggan_servis.Helper.CurrencyHelper
import com.example.pelanggan_servis.Katalog.KatalogDetailActivity
import com.example.pelanggan_servis.Katalog.KatalogMainActivity
import com.example.pelanggan_servis.R
import com.squareup.picasso.Picasso

class AdapterKatalog(val dataKatalog: List<HashMap<String,String>>, val parent: KatalogMainActivity) :
    RecyclerView.Adapter<AdapterKatalog.HolderDataKatalog>(){
    class HolderDataKatalog(v: View) : RecyclerView.ViewHolder(v){
        val cd = v.findViewById<CardView>(R.id.card)
        val nmB = v.findViewById<TextView>(R.id.katalogNama)
        val tgl = v.findViewById<TextView>(R.id.katalogTanggal)
        val jns = v.findViewById<TextView>(R.id.katalogJenis)
        val sts = v.findViewById<TextView>(R.id.katalogStatus)
        val hrg = v.findViewById<TextView>(R.id.katalogHarga)
        val img = v.findViewById<ImageView>(R.id.katalogPhoto)
        val sd = v.findViewById<ImageView>(R.id.imgSold)
        val dtt = v.findViewById<Button>(R.id.btnDetailTransaksi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataKatalog {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_katalog, parent, false)
        return HolderDataKatalog(v)
    }

    override fun getItemCount(): Int {
        return dataKatalog.size
    }

    override fun onBindViewHolder(holder: HolderDataKatalog, position: Int) {
        val data = dataKatalog.get(position)

        holder.nmB.setText(data.get("nama_barang"))
        holder.tgl.setText(data.get("tgl_upload"))
        holder.jns.setText("Jenis :"+data.get("jenis_barang"))
        holder.hrg.text = CurrencyHelper.formatCurrency(data.get("harga_katalog")!!.toInt())
        Picasso.get().load(data.get("img_barang")).into(holder.img)

        val stat = data.get("status_katalog")
        if (stat.toString().equals("Ready")) {
            holder.sts.setText("Barang Tersedia")
            holder.sts.setTextColor(Color.BLUE)

            holder.dtt.setOnClickListener { v: View ->
                val intent = Intent(v.context, KatalogDetailActivity::class.java)
                intent.putExtra("kdB", data.get("kd_barang").toString())
                v.context.startActivity(intent)
                parent.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
            }

        } else if (stat.toString().equals("Sold")) {
            holder.sts.setText("Barang Telah Terjual")
            holder.sts.setTextColor(Color.RED)
            holder.sd.visibility = View.VISIBLE
            holder.hrg.setTextColor(Color.DKGRAY)
            holder.cd.setCardBackgroundColor(Color.parseColor("#EDEDED"))
            holder.dtt.setBackgroundColor(Color.GRAY)
        } else if (stat.toString().equals("Pending")) {
            holder.sts.setText("Barang Telah di Pesan!")
            holder.sts.setTextColor(Color.RED)
            holder.hrg.setTextColor(Color.DKGRAY)
            holder.cd.setCardBackgroundColor(Color.parseColor("#EDEDED"))
            holder.dtt.setBackgroundColor(Color.GRAY)
        }
    }
}