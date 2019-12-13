package com.example.pokedex.dex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokedex.R
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.PokemonRef
import kotlinx.android.synthetic.main.poke_dex_fragment.*

class PokeDex : Fragment() {

    private lateinit var viewModel: PokeDexViewModel
    private val pokemonList = arrayListOf<PokemonRef>()
    private lateinit var pokemon: Pokemon
    private val pokemonAdapter = PokemonAdapter(pokemonList) {pokemon : PokemonRef -> onPokemonClick(pokemon) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.poke_dex_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setListeners()
        initViews()

        viewModel = ViewModelProviders.of(this).get(PokeDexViewModel::class.java)

        viewModel.getPokemonRefs()

        viewModel.getPokemon("1")

        // Observe pokemon from the view model, update the list when the data is changed.
        viewModel.pokemonRefs.observe(this, Observer { pokemonRefs ->
            this.pokemonList.clear()
            this.pokemonList.addAll(pokemonRefs)
            pokemonAdapter.notifyDataSetChanged()
            println("Pokedex $pokemonRefs")
        })

        // Observe pokemon from the view model, update the list when the data is changed.
        viewModel.pokemon.observe(this, Observer { pokemon ->
            this.pokemon = pokemon
            println("POOEKAK $pokemon")
        })
    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        rvPokemon.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        rvPokemon.adapter = pokemonAdapter
    }

    private fun setListeners() {

    }

    private fun onPokemonClick(pokemon: PokemonRef) {
        println("Clicked on! $pokemon")
    }
}
