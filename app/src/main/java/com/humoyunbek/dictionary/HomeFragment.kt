package com.humoyunbek.dictionary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.humoyunbek.dictionary.RoomUtils.AppDatabase
import com.humoyunbek.dictionary.RoomUtils.MyWordDao
import com.humoyunbek.dictionary.RoomUtils.WordDb
import com.humoyunbek.dictionary.adapter.HistoryAdapter
import com.humoyunbek.dictionary.databinding.FragmentHomeBinding
import com.humoyunbek.dictionary.models.WordModel
import com.humoyunbek.dictionary.viewModel.utils.Status
import com.humoyunbek.dictionary.viewModel.utils.UserViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log2

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    interface onSomeEventListener {
        fun someEvent(s: Int?)
    }
    var someEventListener: onSomeEventListener? = null
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        someEventListener = try {
            activity as onSomeEventListener
        } catch (e: java.lang.Exception) {
            throw java.lang.Exception("$activity must implement onSomeEventListener")
        }
    }

    lateinit var binding: FragmentHomeBinding
    lateinit var userViewModel: UserViewModel
    var wordModel: WordModel? = null
    var myWordDb: WordDb? = null
    lateinit var appDatabase: AppDatabase
    lateinit var historyAdapter: HistoryAdapter
    lateinit var savedAdapter: HistoryAdapter
    lateinit var historyList:ArrayList<WordDb>
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    lateinit var linearLayoutManger:LinearLayoutManager

    @SuppressLint("NotifyDataSetChanged", "UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context))
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        appDatabase = AppDatabase.getInstance(context)
        binding.date.text = SimpleDateFormat("dd.MM.yyyy").format(Date())
        historyList = ArrayList()
        historyList.addAll(appDatabase.wordDao().getAllMyWord())
        historyAdapter = HistoryAdapter(context!!,historyList, object : HistoryAdapter.RvClick {
            override fun saveClick(model: WordDb, position: Int) {
//                if (model.save == true){
//                    model.save = false
//                    appDatabase.wordDao().updateWord(model)
//                }else{
//                    model.save = true
//                    appDatabase.wordDao().updateWord(model)
//                }
            }
        })
        linearLayoutManger = LinearLayoutManager(context)
        linearLayoutManger.reverseLayout = true
        linearLayoutManger.stackFromEnd = true
        linearLayoutManger.orientation = LinearLayoutManager.HORIZONTAL
        binding.historyRv.layoutManager = linearLayoutManger
        binding.historyRv.adapter = historyAdapter
        var savedList = java.util.ArrayList<WordDb>()
        for (models in historyList) {
            if (models.save!!){
                savedList.add(models)
            }
        }
        savedAdapter = HistoryAdapter(context!!,savedList, object : HistoryAdapter.RvClick {
            override fun saveClick(model: WordDb, position: Int) {
//                model.save = false
//                savedList.remove(model)
//                appDatabase.wordDao().updateWord(model)
//
//                savedAdapter.notifyItemRemoved(position)
//                savedAdapter.notifyItemRangeChanged(position, savedList.size)
            }
        })
        linearLayoutManger = LinearLayoutManager(context)
        linearLayoutManger.reverseLayout = true
        linearLayoutManger.stackFromEnd = true
        linearLayoutManger.orientation = LinearLayoutManager.HORIZONTAL
        binding.savedRv.layoutManager = linearLayoutManger
        binding.savedRv.adapter = savedAdapter
        binding.humburgerBtn.setOnClickListener {
            someEventListener?.someEvent(1)
        }
        suggestion()
        binding.shareCard.setOnClickListener {
            if (wordModel!=null){
                try {
                    val share = Intent()
                    share.action = Intent.ACTION_SEND
                    share.setType("text/plain")
                    share.putExtra(
                        Intent.EXTRA_TEXT,
                        "${wordModel!!.word} \n" +
                                "${wordModel!!.meanings[0].definitions[0].definition} \n" +
                                "${wordModel!!.meanings[0].definitions[0].example} \n" +
                                "${wordModel!!.meanings[0].definitions[0].synonyms} \n" +
                                "${wordModel!!.phonetics[0].audio} \n"
                    )
                    startActivity(Intent.createChooser(share,"Share..."))
                }catch (e:Exception){
                    Log.e(TAG, "onCreateView: ${e.message}", )
                }
            }else if (myWordDb!=null){
                try {
                    val share = Intent()
                    share.action = Intent.ACTION_SEND
                    share.setType("text/plain")
                    share.putExtra(
                        Intent.EXTRA_TEXT,
                        "${myWordDb!!.word} \n" +
                                "${myWordDb!!.definition} \n" +
                                "${myWordDb!!.example} \n" +
                                "${myWordDb!!.audioLink} \n"
                    )
                    startActivity(Intent.createChooser(share,"Share..."))
                }catch (e:Exception){
                    Log.e(TAG, "onCreateView: ${e.message}", )
                }
            }
        }

        binding.audioCard.setOnClickListener {
            if (wordModel!=null){
                try {
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(
                        binding.root.context,
                        Uri.parse(wordModel!!.phonetics[0].audio)
                    )
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    Toast.makeText(context, "Audio start", Toast.LENGTH_SHORT).show()
                }catch (e:Exception){
                    Log.e(TAG, "onCreateView: ${e.message}", )
                }
            } else if (myWordDb!=null){
                try {
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(
                        binding.root.context,
                        Uri.parse("https:"+ myWordDb!!.audioLink)
                    )
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    Toast.makeText(context, "Audio start", Toast.LENGTH_SHORT).show()
                }catch (e:Exception){
                    Log.e(TAG, "onCreateView: ${e.message}", )
                }
            }
        }

        binding.cardCopy.setOnClickListener {
            copyText()
        }
        binding.copyImg.setOnClickListener {
            copyText()
        }

        binding.savedCard.setOnClickListener {
            var dataList = ArrayList<WordDb>()
            dataList.addAll(appDatabase.wordDao().getAllMyWord())
            for (model in dataList) {
                if (model.word == binding.word.text.toString()){
                    if (model.save!!){
                        model.save = false
                        appDatabase.wordDao().updateWord(model)
                        binding.savedImg.setImageResource(R.drawable.saved2)
                    }else{
                        model.save = true
                        appDatabase.wordDao().updateWord(model)
                        binding.savedImg.setImageResource(R.drawable.bookmark)
                    }
                }
            }
        }

        return binding.root
    }

    private fun suggestion() {
        binding.search.doOnTextChanged { text, start, before, count ->
            binding.word.text = text.toString()

            GlobalScope.launch(Dispatchers.Main) {
                delay(1000)
                userViewModel.getWord(text.toString()).observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.LOADING -> {}
                        Status.ERROR -> {
                            Log.d(TAG, "onCreateView: error")
                        }
                        Status.SUCCESS -> {
                            Log.d(TAG, "onCreateView: ${it.data}")
                            wordModel = it.data?.get(0)
                            sharedPreferences =
                                activity?.getSharedPreferences("Dictionary", Context.MODE_PRIVATE)!!
                            editor = sharedPreferences.edit()
                            editor.putString("word", binding.word.text.toString())
                            editor.commit()
                            writeDb()
                        }
                    }
                }
            }

        }


    }

    override fun onResume() {
        super.onResume()
        appDatabase = AppDatabase.getInstance(context)
        var lists = ArrayList<WordDb>()
        lists.addAll(appDatabase.wordDao().getAllMyWord())
        if (lists.isNotEmpty()){
            myWordDb = lists.last()
        }
        if (myWordDb!=null){
            binding.word.text = myWordDb?.word
            if (myWordDb!!.save!!){
                binding.savedImg.setImageResource(R.drawable.bookmark)
            }else{
                binding.savedImg.setImageResource(R.drawable.saved2)
            }
        }
    }

    private fun copyText() {
        val clipboardManager =
            activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("copy to",binding.search.text.toString()))
    }

    private fun writeDb() {
        val list = appDatabase.wordDao().getAllMyWord()
        var has = true
        if (list.isNotEmpty()){
            list.forEach {
                if (it.word ==wordModel?.word){
                    has = false
                }
            }
            if (has){
                appDatabase.wordDao().addWord(
                    WordDb(
                        wordModel?.word,
                        wordModel!!.meanings[0].definitions[0].definition,
                        wordModel!!.meanings[0].definitions[0].example,
                        wordModel!!.phonetics[0].audio,
                        false,
                        wordModel!!.phonetics[0].text
                    )
                )
            }
        }else{
            appDatabase.wordDao().addWord(
                WordDb(
                    wordModel?.word,
                    wordModel!!.meanings[0].definitions[0].definition,
                    wordModel!!.meanings[0].definitions[0].example,
                    wordModel!!.phonetics[0].audio,
                    false,
                    wordModel!!.phonetics[0].text
                )
            )
        }

    }


}