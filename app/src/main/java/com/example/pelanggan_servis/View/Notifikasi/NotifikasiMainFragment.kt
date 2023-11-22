package com.example.pelanggan_servis.View.Notifikasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.pelanggan_servis.R
import kotlinx.android.synthetic.main.notifikasi_main_fragment.view.*

class NotifikasiMainFragment : Fragment() {
    lateinit var v : View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.notifikasi_main_fragment, container, false)

        v.viewPagerNotifikasi.adapter = PageNotifikasiAdapter(childFragmentManager)
        v.tabLayoutNotifikasi.setupWithViewPager(v.viewPagerNotifikasi)

        return v
    }

    private inner class PageNotifikasiAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> {
                    return FragmentNotifikasiPesan()
                }
                1 -> {
                    return FragmentNotifikasiInformasi()
                }
                else -> {
                    return FragmentNotifikasiPesan()
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> {
                    return "Pesan"
                }
                1 -> {
                    return "Informasi"
                }
            }
            return super.getPageTitle(position)
        }
    }
}