package com.example.pelanggan_servis.PageAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.pelanggan_servis.Katalog.FragmentKatalogAc
import com.example.pelanggan_servis.Katalog.FragmentKatalogHp
import com.example.pelanggan_servis.Katalog.FragmentKatalogLaptop
import com.example.pelanggan_servis.Katalog.FragmentKatalogSemua
import com.example.pelanggan_servis.Katalog.FragmentKatalogTelevisi

class PageBarangAdapter (fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return FragmentKatalogSemua()
            }
            1 -> {
                return FragmentKatalogTelevisi()
            }
            2 -> {
                return FragmentKatalogAc()
            }
            3 -> {
                return FragmentKatalogHp()
            }
            4 -> {
                return FragmentKatalogLaptop()
            }
            else -> {
                return FragmentKatalogSemua()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Semua"
            }
            1 -> {
                return "TV"
            }
            2 -> {
                return "AC"
            }
            3 -> {
                return "HP"
            }
            4 -> {
                return "Laptop"
            }
        }
        return super.getPageTitle(position)
    }

}