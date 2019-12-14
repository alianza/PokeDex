package com.example.pokedex.detail

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pokedex.R
import com.example.pokedex.main.MainActivity
import com.example.pokedex.model.Pokemon
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var pokemon: Pokemon
    private lateinit var mainActivity: MainActivity

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
    }

    private fun setStats() {
        setTypes()
        setBars()
    }

    private fun setBars() {
        pokemon.stats.forEachIndexed{index, stat ->
            println(stat)
            when (stat.stat.name) {
                "hp" -> {
                    pbHp.progress = stat.base_stat
                }
                "defense" -> {
                    pbDefense.progress = stat.base_stat
                }
                "attack" -> {
                    pbAttack.progress = stat.base_stat
                }
                "speed" -> {
                    pbSpeed.progress = stat.base_stat
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
