package com.humoyunbek.dictionary

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.humoyunbek.dictionary.adapter.DefinitionAdapter
import com.humoyunbek.dictionary.databinding.FragmentSearchPagerBinding
import com.humoyunbek.dictionary.models.DefinitionModel
import com.humoyunbek.dictionary.models.WordModel
import com.humoyunbek.dictionary.viewModel.utils.Status
import com.humoyunbek.dictionary.viewModel.utils.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [Search_Pager_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Search_Pager_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    lateinit var binding: FragmentSearchPagerBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var userViewModel: UserViewModel
    lateinit var definitionModel: DefinitionModel
    lateinit var definitionList: ArrayList<DefinitionModel>
    lateinit var definitionAdapter: DefinitionAdapter
    var wordModel: WordModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchPagerBinding.inflate(LayoutInflater.from(context))
        definitionList = ArrayList()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        sharedPreferences =
            activity?.getSharedPreferences("Dictionary", Context.MODE_PRIVATE)!!
        val key = sharedPreferences.getString("word", "")
        Log.d(ContentValues.TAG, "onCreateView: $key")

        GlobalScope.launch(Dispatchers.Main) {
            userViewModel.getWord(key.toString()).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {}
                    Status.ERROR -> {
                        Log.d(ContentValues.TAG, "onCreateView: error")
                    }
                    Status.SUCCESS -> {
                        wordModel = it.data?.get(0)
                        wordfun(wordModel)
                    }
                }
            }
        }


        return binding.root
    }

    private fun wordfun(wordModel: WordModel?) {
        val definitions = wordModel!!.meanings[0].definitions
        for (model in definitions) {
            var definition = model.definition
            var example = model.example
            definitionModel = DefinitionModel(definition, example)
            definitionList.add(definitionModel)
        }
        definitionAdapter = DefinitionAdapter(definitionList)
        binding.definitionRv.adapter = definitionAdapter


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Search_Pager_Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            Search_Pager_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}