package com.ishanvohra.armorx.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ishanvohra.armorx.R
import com.ishanvohra.armorx.databinding.ItemSearchResultBinding
import com.ishanvohra.armorx.models.ArmorResponseItem
import java.util.*

class ArmorPiecesAdapter(val context: Context): RecyclerView.Adapter<ArmorPiecesAdapter.ViewHolder>() {

    var dataSet = mutableListOf<ArmorResponseItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value){
            field = value
            notifyDataSetChanged()
        }


    inner class ViewHolder(val binding: ItemSearchResultBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            with(dataSet[position]){
                Glide.with(context)
                    .load(assets.imageFemale)
                    .into(armorPartImage)

                typeImageView.setImageDrawable(getImageDrawbleId(this.type)?.let {
                    ContextCompat.getDrawable(context,
                        it
                    )
                })

                nameTextView.text = name
                baseDefenceTextView.text = context.getString(R.string.base_defense, defense.base)
                decorationSlotsTextView.text = context.getString(R.string.decoration_slots, slots.size)
                rankTextView.text = context.getString(R.string.rank,
                    rank.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
            }
        }
    }

    private fun getImageDrawbleId(type: String): Int? {
        return when(type){
            "head" -> R.drawable.ic_head
            "chest" -> R.drawable.ic_chest
            "gloves" -> R.drawable.ic_gloves
            "waist" -> R.drawable.ic_waist
            "legs" -> R.drawable.ic_legs
            else -> {null}
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun filterList(query: String) {
        val filteredList = mutableListOf<ArmorResponseItem>()
        val filterPattern = query.lowercase(Locale.ROOT).trim()
        for (item in dataSet) {
            if (item.name.lowercase(Locale.ROOT).contains(filterPattern)) {
                filteredList.add(item)
            }
        }
        dataSet = filteredList
    }

}