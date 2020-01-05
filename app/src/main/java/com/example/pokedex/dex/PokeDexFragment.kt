package com.example.pokedex.dex

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokedex.BuildConfig
import com.example.pokedex.R
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.PokemonRef
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.poke_dex_fragment.*

class PokeDexFragment : Fragment() {

    private lateinit var viewModel: PokeDexViewModel
    private val pokemonRefs = mutableListOf<PokemonRef>()
    private var pokemons = mutableListOf<Pokemon>()
    private var filteredPokemons = pokemons
    private val pokemonAdapter =
        PokemonAdapter(pokemons) { pokemon: Pokemon -> onPokemonClick(pokemon) }

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

        // Check if previously loaded pokemon are still in memory (Don't need to load again)
        if (pokemons.isEmpty() || pokemonRefs.isEmpty()) {
            // Get Pokemon references
            viewModel.getPokemonRefs()

            // Observe pokemon from the view model, update the list when the data is changed.
            viewModel.pokemon.observe(this, Observer { pokemon ->
                    this.pokemons.add(pokemon)
                    this.pokemons.sortBy { it.name }
                    pokemonAdapter.notifyDataSetChanged()
//                    println("SIZE:" + this.pokemons.size + " " + BuildConfig.POKEMONS_TO_LOAD)
                    if (this.pokemons.size == BuildConfig.POKEMONS_TO_LOAD) {
                        hideLoader()
                    }
            })

            viewModel.error.observe(this, Observer { error: String ->
                hideLoader()
                setEmptyView("")
            })

            // Observe pokemon from the view model, update the list when the data is changed.
            viewModel.pokemonRefs.observe(this, Observer { pokemonRefs ->
                this.pokemonRefs.clear()
                this.pokemonRefs.addAll(pokemonRefs)
                this.pokemonRefs.forEach { pokemonRef ->
                    viewModel.getPokemon(pokemonRef.name)
                    pokemonAdapter.notifyDataSetChanged()
                }
            })
        } else {
            hideLoader()
        }
    }

    private fun hideLoader() {
        ViewCompat.animate(flLoader).apply {
            interpolator = AccelerateInterpolator()
            alpha(0f)
            duration = 1000
            withEndAction { flLoader.visibility = View.GONE }
            start()
        }    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter and hide ut until loaded
        rvPokemon.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        rvPokemon.adapter = pokemonAdapter

        // Get withSearch parameter
        val withSearch = arguments?.getBoolean("withSearch")

        if (withSearch!!) {
            openSearchBar()
        }
    }

    private fun setListeners() {
        svSearch.setOnCloseListener {
            dismissKeyboard()
            collapseSearchBar()
        }

        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText); return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                filter(query); return false
            }
        })
    }

    /**
     * Filters list based on user supplied query
     *
     * @param query
     */
    @SuppressLint("DefaultLocale")
    private fun filter(query: String) {
        filteredPokemons = pokemons.filter { pokemon ->
            pokemon.name.toLowerCase().contains(query.toLowerCase())
        } as MutableList<Pokemon>

        pokemonAdapter.pokemons = filteredPokemons
        pokemonAdapter.notifyDataSetChanged()

        setEmptyView(query)
    }

    /**
     * Set's no results view
     *
     */
    private fun setEmptyView(query: String) {
        if (pokemonAdapter.itemCount == 0) {
            if (query.isNotEmpty()) {
                tvNoResults.text = getString(R.string.no_results_search, query)
            } else {
                tvNoResults.text = getString(R.string.no_results)
            }
            tvNoResults.visibility = View.VISIBLE
        } else {
            tvNoResults.visibility = View.GONE
        }
    }

    /**
     * Toggle searchBar state
     *
     */
    fun toggleSearchBar() {
        if (svSearch.height == 0) {
            println("Opened Search!")
            openSearchBar()
        } else {
            println("Closed Search!")
            closeSearchBar()
        }
    }

    /**
     * Opens search bar and sets focus
     *
     */
    private fun openSearchBar() {
        val searchView = svSearch
        val params = searchView.layoutParams
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        searchView.layoutParams = params
        svSearch?.isIconified = false
    }

    /**
     * Collapses search bar, closes keyboard and reset searchView state
     *
     */
    private fun closeSearchBar() {
        svSearch?.isIconified = true
        collapseSearchBar()
        dismissKeyboard()
    }

    /**
     * Collapses searchView element
     *
     * @return success
     */
    private fun collapseSearchBar(): Boolean {
        val searchView = svSearch
        val params = searchView.layoutParams
        params.height = 0
        searchView.layoutParams = params
        return true
    }

    /**
     * Dismisses the keyboard.
     *
     */
    private fun dismissKeyboard() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun onPokemonClick(pokemon: Pokemon) {
        dismissKeyboard()
        val pokemonBundle = bundleOf("pokemon" to pokemon)
//        findNavController().navigate(PokedexFragmentActions, pokemonBundle)
        NavHostFragment.findNavController(navHostFragment).navigate(R.id.action_pokeDex_to_detailFragment, pokemonBundle)
    }
}
