package com.aplikasi.donordarah.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aplikasi.donordarah.fragment.BerandaFragment
import com.aplikasi.donordarah.fragment.CariPendonorFragment
import com.aplikasi.donordarah.fragment.ProfilFragment
import com.aplikasi.donordarah.fragment.RequestFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()
        when(position){
            0 -> fragment = BerandaFragment()
            1 -> fragment = RequestFragment()
            2 -> fragment = CariPendonorFragment()
            3 -> fragment = ProfilFragment()
        }
        return fragment
    }
}