package com.example.pokedex.view.dex

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokedex.BuildConfig
import com.example.pokedex.R
import com.example.pokedex.model.entity.Pokemon
import com.example.pokedex.model.entity.PokemonRef
import kotlinx.android.synthetic.main.poke_dex_fragment.*
import java.util.*
import kotlin.collections.LinkedHashMap

class PokeDexFragment : Fragment() {

    private lateinit var viewModel: PokeDexViewModel
    private val pokemonRefs = mutableListOf<PokemonRef>()
    private var pokemons = mutableListOf<Pokemon>()
    private var filteredPokemons = mutableListOf<Pokemon>()
    private val pokemonAdapter = PokemonAdapter(pokemons) { pokemon: Pokemon -> onPokemonClick(pokemon) }
    private var searchFilters: LinkedHashMap<String, Int> = LinkedHashMap()
    private var pokemonOffset = 0
    private var totalNumberOfPokemon = 0
    private var currentFilter = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.poke_dex_fragment, container, false) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PokeDexViewModel::class.java)

        setListeners()
        initViews()
        getPokemon()
    }

    /**
     * Sets all event listeners
     *
     */
    private fun setListeners() {
        // Observe pokemon from the view model, update the list when the data is changed.
        viewModel.pokemon.observe(viewLifecycleOwner, { pokemon ->
            this.pokemons.add(pokemon)
            if (currentFilter.isNotEmpty()) {
                this.filteredPokemons.add(pokemon)
                filter(currentFilter)
            }
            pokemonAdapter.notifyDataSetChanged()
            if (this.pokemons.size == BuildConfig.POKEMONS_TO_LOAD) {
                hideLoader()
            }
        })

        viewModel.totalNumberOfPokemon.observe(viewLifecycleOwner, { totalNumberOfPokemon ->
            this.totalNumberOfPokemon = totalNumberOfPokemon
        })

        // Observe pokemon from the view model, update the list when the data is changed.
        viewModel.pokemonRefs.observe(viewLifecycleOwner, { pokemonRefs ->
            this.pokemonRefs.clear()
            this.pokemonRefs.addAll(pokemonRefs)
            this.pokemonRefs.forEach { pokemonRef ->
                viewModel.getPokemon(pokemonRef.name)
                pokemonAdapter.notifyDataSetChanged()
            }
        })

        viewModel.error.observe(viewLifecycleOwner, {
            hideLoader()
            setEmptyView("")
        })

        svSearch.setOnCloseListener { dismissKeyboard(); collapseSearchBar() }

        rvPokemon.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    println("SCROLL END")
                    loadPokemon()
                }
            }
        })

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
     * Binds pokemon adapter, sets searchView
     *
     */
    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter and hide ut until loaded
        rvPokemon.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        rvPokemon.adapter = pokemonAdapter

        viewModel.getTotalNumberOfPokemon()

        // Get withSearch parameter
        val withSearch = arguments?.getBoolean("withSearch")

        if (withSearch!!) {
            openSearchBar()
        }
    }

    /**
     * Get's all pokemon references and then individual pokemons and adds them to list
     *
     */
    private fun getPokemon() {
        searchFilters["offset"] = 0
        searchFilters["limit"] = BuildConfig.POKEMONS_TO_LOAD

        // Check if previously loaded pokemon are still in memory (Don't need to load again)
        if (pokemons.isEmpty() || pokemonRefs.isEmpty()) {
            // Get Pokemon references
            viewModel.getPokemonRefs(searchFilters)

        } else {
            hideLoader()
        }
    }

    private fun loadPokemon() {
        if (this.pokemons.size < totalNumberOfPokemon) {

            pokemonOffset += BuildConfig.POKEMONS_TO_LOAD
            searchFilters["offset"] = pokemonOffset

            viewModel.getPokemonRefs(searchFilters)
        }
    }

    private fun hideLoader() {
        ViewCompat.animate(flLoader).apply {
            interpolator = AccelerateInterpolator()
            alpha(0f)
            duration = 1000
            withEndAction { flLoader?.visibility = View.GONE }
            start()
        }
    }

    /**
     * Filters list based on user supplied query
     *
     * @param query
     */
    private fun filter(query: String) {
        currentFilter = query
        if (currentFilter !== "") {
            filteredPokemons = pokemons.filter { pokemon ->
                pokemon.name.toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
            } as MutableList<Pokemon>
            pokemonAdapter.pokemons = filteredPokemons.distinctBy { it.name }.toMutableList()
        } else {
            pokemonAdapter.pokemons = pokemons.distinctBy { it.name }.toMutableList()
        }
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
                tvNoSavedPokemon.text = getString(R.string.no_results_search, query)
            } else {
                tvNoSavedPokemon.text = getString(R.string.no_results)
            }
            tvNoSavedPokemon.visibility = View.VISIBLE
        } else {
            tvNoSavedPokemon.visibility = View.GONE
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
        if (pokemonOffset + BuildConfig.POKEMONS_TO_LOAD < totalNumberOfPokemon) {
            searchFilters["offset"] = 0
            searchFilters["limit"] = this.totalNumberOfPokemon
            this.pokemonOffset = this.totalNumberOfPokemon

            viewModel.getPokemonRefs(searchFilters)
        }

        val params = svSearch.layoutParams
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        svSearch.layoutParams = params
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
        findNavController().navigate(R.id.action_pokeDex_to_detailFragment, pokemonBundle)
    }
}
