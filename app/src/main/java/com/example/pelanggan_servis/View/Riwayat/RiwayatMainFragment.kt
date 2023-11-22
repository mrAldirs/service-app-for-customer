package com.example.pelanggan_servis.View.Riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.pelanggan_servis.R
import kotlinx.android.synthetic.main.riwayat_main_fragment.view.*

class RiwayatMainFragment : Fragment() {
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.riwayat_main_fragment, container, false)

        v.viewPagerRiwayat.adapter = PageRiwayatAdapter(childFragmentManager)
        v.tabLayoutRiwayat.setupWithViewPager(v.viewPagerRiwayat)

        return v
    }

    private inner class PageRiwayatAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int {
            return 2 // Jumlah halaman yang ingin ditampilkan
        }

        override fun getItem(position: Int): Fragment {
            // Mengembalikan fragment yang sesuai dengan posisi halaman
            return when (position) {
                0 -> FragmentRiwayatProses()
                1 -> FragmentRiwayatSelesai()
                else -> FragmentRiwayatProses() // Halaman default
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> {
                    return "Proses"
                }
                1 -> {
                    return "Selesai"
                }
            }
            return super.getPageTitle(position)
        }
    }
}