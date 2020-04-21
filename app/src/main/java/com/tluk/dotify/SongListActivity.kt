package com.tluk.dotify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ericchee.songdataprovider.Song
import com.ericchee.songdataprovider.SongDataProvider
import com.tluk.dotify.MainActivity.Companion.SONG_KEY
import kotlinx.android.synthetic.main.activity_song_list.*

class SongListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)

        val listOfSongs = SongDataProvider.getAllSongs().toMutableList()
        val songAdapter = SongListAdapter(listOfSongs)

        songAdapter.onSongClickListener = { someSong: Song ->
            tvCurrentSong.text = someSong.title + " - " + someSong.artist
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(SONG_KEY, someSong)
            startActivity(intent)
        }

        btnShuffle.setOnClickListener {
            val newSongList = listOfSongs.apply {
                shuffle()
            }
            songAdapter.change(newSongList)
        }
        rvSongList.adapter = songAdapter
    }

}

