package com.example.pelanggan_servis.View.Mekanik

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pelanggan_servis.Adapter.AdapterMekanik
import com.example.pelanggan_servis.Beli.BeliMainActivity
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.ViewModel.MekanikViewModel
import com.example.pelanggan_servis.databinding.MekanikMainActivityBinding

class MekanikMainActivity : AppCompatActivity() {
    private lateinit var binding: MekanikMainActivityBinding
    lateinit var mekanikViewModel: MekanikViewModel
    lateinit var adapter: AdapterMekanik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MekanikMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mekanikViewModel = ViewModelProvider(this).get(MekanikViewModel::class.java)
        adapter = AdapterMekanik(ArrayList())
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing) // Mengambil nilai spacing dari dimens.xml

        val includeEdge = true // Atur true jika Anda ingin padding pada tepi luar grid
        val itemDecoration = GridSpacingItemDecoration(2, spacingInPixels, includeEdge)
        binding.recyclerView.addItemDecoration(itemDecoration)
        binding.recyclerView.adapter = adapter

        binding.btnBack.setOnClickListener { onBackPressed() }

        binding.btnSearch.setOnClickListener {
            loadData(binding.textSearch.text.toString().trim())
        }
    }

    override fun onStart() {
        super.onStart()
        loadData("")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun loadData(nm: String) {
        mekanikViewModel.loadData(nm).observe(this, Observer { dataList ->
            adapter.setData(dataList)
        })
    }

    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // Mendapatkan posisi item
            val column = position % spanCount // Mendapatkan kolom saat ini

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 2) * spacing / spanCount

                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 2) * spacing / spanCount

                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }
}