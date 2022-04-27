package com.humoyunbek.dictionary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.humoyunbek.dictionary.RoomUtils.AppDatabase
import com.humoyunbek.dictionary.RoomUtils.WordDb
import com.humoyunbek.dictionary.adapter.HistoryAdapter
import com.humoyunbek.dictionary.databinding.FragmentHistoryBinding
import com.humoyunbek.dictionary.databinding.HistoryItemBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {

    lateinit var binding: FragmentHistoryBinding
    lateinit var historyAdapter: HistoryAdapter
    lateinit var historyList:ArrayList<WordDb>
    lateinit var appDatabase: AppDatabase
    lateinit var linearLayoutManager:LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(LayoutInflater.from(context))
        appDatabase = AppDatabase.getInstance(context)
        historyList = ArrayList()
        historyList.addAll(appDatabase.wordDao().getAllMyWord())
        historyAdapter = HistoryAdapter(requireContext(),historyList,object :HistoryAdapter.RvClick{
            override fun saveClick(model: WordDb, position: Int) {
                var dataList = ArrayList<WordDb>()
                dataList.addAll(appDatabase.wordDao().getAllMyWord())
                for (modelList in dataList) {
                    if (modelList.word == model.word){
                        if (modelList.save!!){
                            modelList.save = false
                            appDatabase.wordDao().updateWord(modelList)
                        }else{
                            modelList.save = true
                            appDatabase.wordDao().updateWord(modelList)
                        }
                    }
                }
            }

        })
        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.historyFragmentRv.layoutManager = linearLayoutManager
        binding.historyFragmentRv.adapter = historyAdapter
        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }


}