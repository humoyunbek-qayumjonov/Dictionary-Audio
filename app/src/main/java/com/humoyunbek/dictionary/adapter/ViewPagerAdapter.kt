package com.humoyunbek.dictionary.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.humoyunbek.dictionary.ViewPagerFragment
import com.humoyunbek.dictionary.model.PagerModel

class ViewPagerAdapter(var list:ArrayList<PagerModel>, fragmentManager: FragmentManager):FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return ViewPagerFragment.newInstance(list[position])
    }
}