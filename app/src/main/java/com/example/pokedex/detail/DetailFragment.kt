package com.example.pokedex.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.main.MainActivity
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.SavedPokemon
import com.example.pokedex.model.Species
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var pokemon: Pokemon
    private lateinit var mainActivity: MainActivity
    private lateinit var species: Species
    private lateinit var savedPokemon: SavedPokemon
    private var currentImageIsFront = false
    private var initialUpdate = false
    private var initialSavedPokemon = false
    private var types = mutableListOf<String>()
    private val typesAdapter = TypesAdapter(types)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Determine whether pokemon is passed as argument or pokemon has to be fetched based on name
        if (arguments?.containsKey("pokemon")!!) {
            pokemon = arguments!!.getParcelable("pokemon")!!
        }
        if (arguments?.containsKey("savedPokemon")!!) {
            savedPokemon = arguments!!.getParcelable("savedPokemon")!!
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        mainActivity = activity as MainActivity
        ibCatch.setTag(R.id.CAUGHT_TAG, false) // Key represents catch state

        if (arguments?.containsKey("pokemon")!!) {
            initViews()
        } else {
            viewModel.getPokemon(savedPokemon.name)

            // Observe pokemon from the view model, update the list when the data is changed.
            viewModel.pokemon.observe(this, Observer { pokemon ->
                if (pokemon != null) {
                    this.pokemon = pokemon
                    initViews()
                }
            })
        }
    }

    @SuppressLint("DefaultLocale")
    private fun initViews() {
        mainActivity.setActionBarTitle(pokemon.name.capitalize())

        // Get pokemon species
        viewModel.getSpecies(pokemon.species.url.substringAfter("species/"))

        // Get saved pokemon to check caught status
        viewModel.getSavedPokemon(pokemon.name)

        viewModel.savedPokemon.observe(this, Observer { savedPokemon ->
            if (savedPokemon != null) {
                this.savedPokemon = savedPokemon
                // Update caught status of pokemon once
                if (!initialUpdate) {
                    updateCaughtStatus(savedPokemon.caught, showSnackbar = false)
                    initialUpdate = true
                }
            // If there is no saved pokemon save it once with caught status false
            } else if (!initialSavedPokemon) {
                val pokemonToSave = SavedPokemon(pokemon.name, pokemon.sprites.front_poster, false)
                this.savedPokemon = pokemonToSave
                viewModel.insertSavedPokemon(pokemonToSave)
                initialSavedPokemon = true
            }
        })

        // Initialize the recycler view with a linear layout manager, adapter
        rvTypes.layoutManager = StaggeredGridLayoutManager(1, RecyclerView.HORIZONTAL)
        rvTypes.adapter = typesAdapter

        setStats()

        setListeners()
    }

    private fun setListeners() {
        ibRotate.setOnClickListener { rotatePokemonImage() }

        ivPokemon.setOnClickListener { rotatePokemonImage() }

        ibCatch.setOnClickListener { onCatchButtonClick() }
    }

    @SuppressLint("DefaultLocale")
    private fun updateCaughtStatus(caught: Boolean, showSnackbar: Boolean) {
        val snackBarText: String = if (caught) {
            ibCatch.setImageDrawable(context?.getDrawable(R.drawable.ic_launcher_foreground))
            ibCatch.setTag(R.id.CAUGHT_TAG, true) // Key represents catch state
            getString(R.string.caught, pokemon.name.capitalize())
        } else {
            ibCatch.setImageDrawable(context?.getDrawable(R.drawable.ic_open_pokeball))
            ibCatch.setTag(R.id.CAUGHT_TAG, false) // Key represents catch state
            getString(R.string.not_caught, pokemon.name.capitalize())
        }
        viewModel.updateSavedPokemon(this.savedPokemon)
        if (showSnackbar) { Snackbar.make(clDetail, snackBarText, Snackbar.LENGTH_SHORT).show() }
    }

    private fun onCatchButtonClick() {
        if (ibCatch.getTag(R.id.CAUGHT_TAG) as Boolean) {
            this.savedPokemon.caught = false
            updateCaughtStatus(caught = false, showSnackbar = true)
        } else {
            this.savedPokemon.caught = true
            updateCaughtStatus(caught = true, showSnackbar = true)
        }
    }

    private fun rotatePokemonImage() {
        setImage()
    }

    private fun setStats() {
        setImage()
        setTypes()
        setBars()
        setSpecies()
        setProfile()
        hideLoader()
    }

    private fun hideLoader() {
        ViewCompat.animate(flLoader).apply {
            interpolator = AccelerateInterpolator()
            alpha(0f)
            duration = 500
            withEndAction { flLoader.visibility = View.GONE }
            start()
        }
    }

    private fun setProfile() {
        val height = pokemon.height.toDouble() / 10
        tvHeight.text = getString(R.string.height, height.toString())

        val weight = pokemon.weight.toDouble() / 10
        tvWeight.text = getString(R.string.weight, weight.toString())

        tvExperience.text = getString(R.string.experience, pokemon.base_experience.toString())
    }

    private fun setSpecies() {
        var speciesText = ""
        // Observe species from the view model, concatenate flavor texts
        viewModel.species.observe(this, Observer { species ->
            this.species = species
            this.species.flavor_text_entries.forEach { flavorText ->
                if (flavorText.language.name == "en") {
                    flavorText.text = flavorText.text.replace("\n", " ")
                    flavorText.text = flavorText.text.trim()
                    if (!speciesText.contains(flavorText.text)) {
                        speciesText += " ${flavorText.text}"
                    }
                }
            }
            tvDescrption.text = speciesText.trim()
        })
    }

    private fun setImage() {
        currentImageIsFront = if (currentImageIsFront) {
            Glide.with(context!!).load(pokemon.sprites.back_poster).into(ivPokemon)
            false
        } else {
            Glide.with(context!!).load(pokemon.sprites.front_poster).into(ivPokemon)
            true
        }
    }

    private fun setBars() {
        pokemon.stats.sortBy { it.base_stat }
        setProgressBarsMax(pokemon.stats.last().base_stat)
        pokemon.stats.forEach{ stat ->
            when (stat.stat.name) {
                "hp" -> {
                    pbHp.progress = stat.base_stat
                    tvHPDigit.text = stat.base_stat.toString()
                }
                "defense" -> {
                    pbDefense.progress = stat.base_stat
                    tvDefenseDigit.text = stat.base_stat.toString()
                }
                "attack" -> {
                    pbAttack.progress = stat.base_stat
                    tvAttackDigit.text = stat.base_stat.toString()
                }
                "speed" -> {
                    pbSpeed.progress = stat.base_stat
                    tvSpeedDigit.text = stat.base_stat.toString()
                }
            }
        }
    }

    private fun setProgressBarsMax(value: Int) {
        pbAttack.max = value
        pbSpeed.max = value
        pbDefense.max = value
        pbHp.max = value
    }

    @SuppressLint("DefaultLocale")
    private fun setTypes() {
        pokemon.types.forEach {
            types.add(it.type.name.capitalize())
            typesAdapter.notifyDataSetChanged()
        }
    }
}
