package com.example.pokedex.dex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.data.PokeRepository
import com.example.pokedex.model.PokeResult
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.PokemonRef
import kotlinx.android.synthetic.main.pokemon_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

@Suppress("DEPRECATION")
class PokemonAdapter(
    private val pokemonsRefs: List<PokemonRef>,
    private val onClick: (PokemonRef) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return pokemonsRefs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pokemonsRefs[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { onClick(pokemonsRefs[adapterPosition]) }
        }

        fun bind(pokemon: PokemonRef) {
            itemView.tvPokemon.text = pokemon.name

//            Glide.with(context).load(baseUrl + movie.poster).into(itemView.ivPokemon)

            println("Adapter! " + pokemon.name)
        }
    }
}