package com.example.pokedex.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class SavedPokemon (
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "poster_url") var poster_url: String,
    @ColumnInfo(name = "caught") var caught: Boolean
): Parcelable