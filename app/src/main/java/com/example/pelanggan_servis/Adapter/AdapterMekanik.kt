package com.example.pelanggan_servis.Adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pelanggan_servis.Model.MekanikModel
import com.example.pelanggan_servis.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class AdapterMekanik (private var dataList: List<MekanikModel>)
    : RecyclerView.Adapter<AdapterMekanik.HolderDataTransaksi>() {
    class HolderDataTransaksi(v: View) : RecyclerView.ViewHolder(v) {
        val nm = v.findViewById<TextView>(R.id.mekanikNama)
        val rtB = v.findViewById<MaterialRatingBar>(R.id.mekanikRatingBar)
        val rtT = v.findViewById<TextView>(R.id.mekanikTextRating)
        val img = v.findViewById<CircleImageView>(R.id.mekanikPhoto)
        val cd = v.findViewById<Button>(R.id.mekanikBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataTransaksi {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_mekanik, parent, false)
        return HolderDataTransaksi(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataTransaksi, position: Int) {
        val data = dataList.get(position)

        holder.nm.setText(data.nama_mekanik)
        holder.rtB.rating = data.average_mekanik!!
        holder.rtT.setText(data.average_mekanik.toString())
        Picasso.get().load(data.foto_mekanik).into(holder.img)

        holder.cd.setOnClickListener {
//            val intent = Intent(it.context, ServisDetailActivity::class.java)
//            intent.putExtra("kode", data.kd_transaksi)
//            it.context.startActivity(intent)
        }
    }

    fun setData(newDataList: List<MekanikModel>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}