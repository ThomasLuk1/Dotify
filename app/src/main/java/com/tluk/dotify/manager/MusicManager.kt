package com.tluk.dotify.manager

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.tluk.dotify.model.MusicLibrary
import com.tluk.dotify.model.Song


class MusicManager(context: Context) {

    var songHistory = emptyList<Song>().toMutableList()
    private val queue: RequestQueue = Volley.newRequestQueue(context)

    fun addToHistory(song: Song) {
        songHistory.add(song)
    }

    fun removeFromHistory(): Song {
        return songHistory.removeAt(songHistory.size - 1)
    }

    fun songHistorySize(): Int {
        return songHistory.size
    }

    fun getMusicLibrary(onSongsReady: (MusicLibrary) -> Unit, onError: (() -> Unit)? = null) {
        val url = "https://raw.githubusercontent.com/echeeUW/codesnippets/master/musiclibrary.json"
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                // success
                    val gson = Gson()
                    val musicLibrary = gson.fromJson(response, MusicLibrary::class.java)
                    onSongsReady(musicLibrary)
            },
            { error ->
                //error
                Log.e("musicmanager", "Error occured: ${error.networkResponse.statusCode}")
                onError?.invoke()
            }
        )
        queue.add(request)
    }
}