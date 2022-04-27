package com.humoyunbek.dictionary

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.humoyunbek.dictionary.RoomUtils.AppDatabase
import com.humoyunbek.dictionary.adapter.DefinitionAdapter
import com.humoyunbek.dictionary.adapter.SearchPagerAdapter
import com.humoyunbek.dictionary.databinding.FragmentSearchBinding
import com.humoyunbek.dictionary.models.DefinitionModel
import com.humoyunbek.dictionary.models.WordModel
import com.humoyunbek.dictionary.viewModel.utils.Status
import com.humoyunbek.dictionary.viewModel.utils.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    lateinit var binding:FragmentSearchBinding
    lateinit var list:ArrayList<String>
    lateinit var searchPagerAdapter: SearchPagerAdapter
    lateinit var userViewModel: UserViewModel
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(LayoutInflater.from(context))
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        list = ArrayList()
        list.add("Description")
        list.add("Other")

        searchPagerAdapter = SearchPagerAdapter(list, requireActivity())
        binding.searchViewPager.adapter = searchPagerAdapter

        TabLayoutMediator(
            binding.searchTabLayout,
            binding.searchViewPager,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.text = list[position]
                }

            }
        ).attach()
        sharedPreferences =
            activity?.getSharedPreferences("Dictionary",Context.MODE_PRIVATE)!!
        val key = sharedPreferences.getString("word","")
        Log.d(TAG, "onCreateView: $key")
        binding.headSearchText.text = key

        return binding.root
    }


}