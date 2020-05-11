package com.tluk.dotify.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MusicLibrary(
    val title: String,
    val numOfSongs: Int,
    val songs: List<Song>
) : Parcelable