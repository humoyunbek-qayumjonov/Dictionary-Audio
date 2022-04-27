package com.humoyunbek.dictionary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humoyunbek.dictionary.databinding.DefinitionLayoutBinding
import com.humoyunbek.dictionary.models.DefinitionModel
import com.humoyunbek.dictionary.models.WordModel

class DefinitionAdapter(var list:ArrayList<DefinitionModel>):RecyclerView.Adapter<DefinitionAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding:DefinitionLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind(model: DefinitionModel,position:Int){
            binding.textNumber.text = (position+1).toString()
            binding.definitionText.text = model.definition
            binding.exampleText.text = model.example
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater =
            DefinitionLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position],position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}