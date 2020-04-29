package com.tluk.dotify.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ericchee.songdataprovider.Song
import com.tluk.dotify.R
import com.tluk.dotify.SongListAdapter
import com.tluk.dotify.fragment.NowPlayingFragment.Companion.ARG_SONG
import kotlinx.android.synthetic.main.activity_song_list.*

class SongListFragment : Fragment() {

    private lateinit var songAdapter: SongListAdapter

    private var onSongClickListener: OnSongClickListener? = null
    private lateinit var listOfSongs: MutableList<Song>
    private lateinit var currSong: Song

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnSongClickListener) {
            onSongClickListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->
            val listOfSongs = args.getParcelableArrayList<Song>(ARG_LIST)
            if (listOfSongs != null) {
                this.listOfSongs = listOfSongs.toMutableList()
            }

        }
        songAdapter = SongListAdapter(listOfSongs)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_songs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songAdapter = SongListAdapter(listOfSongs)
        rvSongList.adapter = songAdapter

        songAdapter.onSongClickListener = { song ->
            tvCurrentSong.text = song.title + " - " + song.artist
            currSong = song
        }

        nowPlayingBar.setOnClickListener {
                onSongClickListener?.onSongSelected(currSong)

        }

        btnShuffle.setOnClickListener {
            shuffleList()
        }
    }


    fun shuffleList() {
        val newSongList = listOfSongs.apply {
            shuffle()
        }
        songAdapter.change(newSongList)
        rvSongList.adapter = songAdapter
    }


    companion object {
        const val ARG_LIST = "ARG_LIST"
    }
}

interface OnSongClickListener {
    fun onSongSelected(song: Song)
}