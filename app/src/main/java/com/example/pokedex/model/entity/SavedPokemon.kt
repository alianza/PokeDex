package com.example.pokedex.model.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class SavedPokemon (
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "poster_url") var poster_url: String?,
    @ColumnInfo(name = "caught_date") var caught_date: Date,
    @ColumnInfo(name = "caught") var caught: Boolean
): Parcelable