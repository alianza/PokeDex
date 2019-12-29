package com.example.pokedex.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.main.MainActivity
import com.example.pokedex.model.Pokemon
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var pokemon: Pokemon
    private lateinit var mainActivity: MainActivity
    private var currentImageisFront = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        pokemon = arguments?.getParcelable("pokemon")!!
        mainActivity = activity as MainActivity

        initViews()
    }

    private fun initViews() {
        mainActivity.setActionBarTitle(pokemon.name)

        println(pokemon)

        setStats()

        setListeners()
    }

    private fun setListeners() {
        ibRotate.setOnClickListener { onRotateButtonClick() }
    }

    private fun onRotateButtonClick() {
        setImage()
    }

    private fun setStats() {
        setImage()
        setTypes()
        setBars()
    }

    private fun setImage() {
        currentImageisFront = if (currentImageisFront) {
            Glide.with(context!!).load(pokemon.sprites.back_poster).into(ivPokemon)
            false
        } else {
            Glide.with(context!!).load(pokemon.sprites.front_poster).into(ivPokemon)
            true
        }
    }

    private fun setBars() {
        pokemon.stats.forEachIndexed{index, stat ->
            println(stat)
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

    private fun setTypes() {
        var typesText = ""

        pokemon.types.forEach {
            typesText += it.type.name + ", "
        }
        tvTypes.text = typesText.dropLast(2)
    }

}
