package com.example.pokedex.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import kotlinx.android.synthetic.main.type_item.view.*


@Suppress("DEPRECATION")
class TypesAdapter(var types: List<String>) : RecyclerView.Adapter<TypesAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.type_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return types.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(types[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(type: String) {
            itemView.tvType.text = type
            setTypeColor(type)
            println("Adapter! $type")
        }

        private fun setTypeColor(type: String) {
            when (type) {
                "Psychic" -> {
                    itemView.tvType.setBackgroundColor(getColor(context ,R.color.psychic))
                }
                "Grass" -> {
                    itemView.tvType.setBackgroundColor(getColor(context ,R.color.grass))
                }
                "Poison" -> {
                    itemView.tvType.setBackgroundColor(getColor(context ,R.color.poison))
                }
                "Water" -> {
                    itemView.tvType.setBackgroundColor(getColor(context ,R.color.water))
                }
                "Fire" -> {
                    itemView.tvType.setBackgroundColor(getColor(context ,R.color.fire))
                }
                "Fighting" -> {
                    itemView.tvType.setBackgroundColor(getColor(context ,R.color.fighting))
                }
                "Rock" -> {
                    itemView.tvType.setBackgroundColor(getColor(context ,R.color.rock))
                }
                "Bug" -> {
                    itemView.tvType.setBackgroundColor(getColor(context ,R.color.bug))
                }
                "Electric" -> {
                    itemView.tvType.setBackgroundColor(getColor(context ,R.color.electric))
                }
            }
        }
    }
}