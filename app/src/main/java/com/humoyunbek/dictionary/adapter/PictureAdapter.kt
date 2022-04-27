package com.humoyunbek.dictionary.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.humoyunbek.dictionary.PictureViewPagerFragment
import com.humoyunbek.dictionary.model.PagerModel

class PictureAdapter(var list:ArrayList<Int>, fragmentManager: FragmentManager):
    FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return PictureViewPagerFragment.newInstance(list[position])
    }
}