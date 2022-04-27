package com.humoyunbek.dictionary.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.humoyunbek.dictionary.Search_Pager_Fragment

class SearchPagerAdapter(var list:ArrayList<String>,fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return Search_Pager_Fragment.newInstance(list[position])
    }
}