package com.example.pokedex.myPokemon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.model.SavedPokemon
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.pokemon_item.view.*
import kotlinx.android.synthetic.main.saved_pokemon_item.view.*

@Suppress("DEPRECATION")
class SavedPokemonAdapter(
    var savedPokemons: List<SavedPokemon>,
    private val onClick: (SavedPokemon) -> Unit
) : RecyclerView.Adapter<SavedPokemonAdapter.ViewHolder>() {

    private lateinit var context: Context

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

        fun bind(savedPokemon: SavedPokemon) {
            itemView.tvSavedPokemon.text = savedPokemon.name
            Glide.with(context).load(savedPokemon.poster_url).into(itemView.ivSavedPokemon)
        }
    }
}