package com.humoyunbek.dictionary

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.humoyunbek.dictionary.RoomUtils.AppDatabase
import com.humoyunbek.dictionary.RoomUtils.WordDb
import com.humoyunbek.dictionary.adapter.HistoryAdapter
import com.humoyunbek.dictionary.databinding.FragmentSaveBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SaveFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaveFragment : Fragment() {

    lateinit var binding: FragmentSaveBinding
    lateinit var saveAdapter:HistoryAdapter
    lateinit var saveList:ArrayList<WordDb>
    lateinit var appDatabase: AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        appDatabase = AppDatabase.getInstance(context)
        saveList = ArrayList()
        var sortedList = ArrayList<WordDb>()
        sortedList.addAll(appDatabase.wordDao().getAllMyWord())
        sortedList.forEach {
            if (it.save == true){
                saveList.add(it)
            }
        }
        saveAdapter = HistoryAdapter(requireContext(),saveList, object : HistoryAdapter.RvClick {
            override fun saveClick(model: WordDb, position: Int) {
                model.save = false
                saveList.remove(model)
                appDatabase.wordDao().updateWord(model)
                saveAdapter.notifyItemRemoved(position)
                saveAdapter.notifyItemRangeChanged(position, saveList.size)
            }

        })
        binding.savedFragmentRv.adapter = saveAdapter
    }


}