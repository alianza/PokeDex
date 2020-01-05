package com.example.pokedex.myPokemon

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.model.SavedPokemon
import kotlinx.android.synthetic.main.saved_pokemon_item.view.*

@Suppress("DEPRECATION")
class SavedPokemonAdapter(
    var savedPokemons: List<SavedPokemon>,
    private val onClick: (SavedPokemon) -> Unit
) : RecyclerView.Adapter<SavedPokemonAdapter.ViewHolder>() {

    private lateinit var context: Context

    private val MONTH_OFFSET = 1
    private val YEAR_OFFSET = 1900

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.saved_pokemon_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return savedPokemons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(savedPokemons[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { onClick(savedPokemons[adapterPosition]) }
        }

        @SuppressLint("DefaultLocale")
        fun bind(savedPokemon: SavedPokemon) {
            itemView.tvSavedPokemonName.text = savedPokemon.name.capitalize()
            itemView.tvSavedPokemonDate.text = context.getString(
                R.string.date_format,
                savedPokemon.caught_date.date.toString(),
                (savedPokemon.caught_date.month + MONTH_OFFSET).toString(),
                (savedPokemon.caught_date.year + YEAR_OFFSET).toString()
            )
            Glide.with(context).load(savedPokemon.poster_url).into(itemView.ivSavedPokemon)
        }
    }
}