package com.humoyunbek.dictionary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humoyunbek.dictionary.R
import com.humoyunbek.dictionary.RoomUtils.AppDatabase
import com.humoyunbek.dictionary.RoomUtils.WordDb
import com.humoyunbek.dictionary.databinding.HistoryItemBinding
lateinit var appDatabase: AppDatabase
class HistoryAdapter(var context:Context,var list:ArrayList<WordDb>,val rvClick: RvClick):RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding:HistoryItemBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind(model: WordDb,position:Int){
            binding.rvHistoryText.text = model.word
            binding.rvReadText.text = model.reading
            appDatabase = AppDatabase.getInstance(context)
            var lists = appDatabase.wordDao().getAllMyWord()
            if (model.save == true){
               binding.rvSavedImg.setImageResource(R.drawable.bookmark)
            }else{
                binding.rvSavedImg.setImageResource(R.drawable.saved2)
            }

            binding.rvSavedImg.setOnClickListener {
                if (model.save == true){
                    model.save = false
                    binding.rvSavedImg.setImageResource(R.drawable.saved2)
                    appDatabase.wordDao().updateWord(model)
                    notifyItemChanged(position)
                }else{
                    model.save = true
                    binding.rvSavedImg.setImageResource(R.drawable.bookmark)
                    appDatabase.wordDao().updateWord(model)
                    notifyItemChanged(position)
                }
            }

//            binding.rvSavedImg.setOnClickListener {
//                rvClick.saveClick(model,position)
//                if (model.save==true){
//                    binding.rvSavedImg.setImageResource(R.drawable.saved2)
//                }else{
//                    binding.rvSavedImg.setImageResource(R.drawable.bookmark)
//                }
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = HistoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position],position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface RvClick{
        fun saveClick(model: WordDb, position: Int)
    }
}