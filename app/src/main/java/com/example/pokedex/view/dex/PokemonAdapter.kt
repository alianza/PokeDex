package com.example.pokedex.view.dex

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.model.entity.Pokemon
import kotlinx.android.synthetic.main.pokemon_item.view.*
import java.util.*


@Suppress("DEPRECATION")
class PokemonAdapter(var pokemons: MutableList<Pokemon>, private val onClick: (Pokemon) -> Unit)
    : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pokemons[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { onClick(pokemons[adapterPosition]) }
        }

        @SuppressLint("DefaultLocale")
        fun bind(pokemon: Pokemon) {
            itemView.tvPokemon.text = pokemon.name.capitalize()
            Glide.with(context).load(pokemon.sprites.front_poster).into(itemView.ivPokemon)
        }
    }
}