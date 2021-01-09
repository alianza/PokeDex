package com.example.pokedex.view.myPokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokedex.R
import com.example.pokedex.model.entity.SavedPokemon
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.my_pokemon_fragment.*

class MyPokemonFragment : Fragment() {

    private lateinit var viewModel: MyPokemonViewModel
    private var savedPokemons = arrayListOf<SavedPokemon>()
    private val savedPokemonAdapter =
        SavedPokemonAdapter(savedPokemons) { savedPokemon: SavedPokemon -> onPokemonClick(savedPokemon) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_pokemon_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyPokemonViewModel::class.java)

        // Initialize the recycler view with a linear layout manager, adapter
        rvSavedPokemon.layoutManager = StaggeredGridLayoutManager(1, RecyclerView.VERTICAL)
        rvSavedPokemon.adapter = savedPokemonAdapter

        viewModel.savedPokemon.observe(viewLifecycleOwner, { savedPokemons ->
            if (savedPokemons != null) {
                this.savedPokemons.clear()
                savedPokemons.forEach{savedPokemon -> if (savedPokemon.caught) {
                    this.savedPokemons.add(savedPokemon)
                    println(savedPokemon)
                    }
                }
                this.savedPokemons.sortByDescending { it.caught_date }
                savedPokemonAdapter.notifyDataSetChanged()
                if (this.savedPokemons.isEmpty()) {
                    tvNoSavedPokemon.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun onPokemonClick(savedPokemon: SavedPokemon) {
        val savedPokemonBundle = bundleOf("savedPokemon" to savedPokemon)

        NavHostFragment.findNavController(navHostFragment).navigate(R.id.action_myPokemonFragment_to_detailFragment, savedPokemonBundle)
    }
}
