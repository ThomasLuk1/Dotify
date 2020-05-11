package com.tluk.dotify.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.tluk.dotify.model.Song
import com.tluk.dotify.DotifyApp
import com.tluk.dotify.R
import com.tluk.dotify.manager.MusicManager
import kotlinx.android.synthetic.main.activity_main.btnBack
import kotlinx.android.synthetic.main.activity_main.btnNext
import kotlinx.android.synthetic.main.activity_main.btnPlay
import kotlinx.android.synthetic.main.activity_main.ivCover
import kotlinx.android.synthetic.main.activity_main.tvArtist
import kotlinx.android.synthetic.main.activity_main.tvNumberOfPlays
import kotlinx.android.synthetic.main.activity_main.tvTitle
import kotlin.random.Random

class NowPlayingFragment : Fragment() {
    private lateinit var song: Song
    private var numPlays = Random.nextInt(1000, 10000)
    private lateinit var listOfSongs: MutableList<Song>
    private lateinit var musicManager: MusicManager

    companion object {
        const val ARG_SONG = "ARG_SONG"
        const val ARG_LIST = "ARG_LIST"
        val TAG:String = NowPlayingFragment::class.java.simpleName
        const val NUM_PLAYS = "num_plays"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        musicManager = (context.applicationContext as DotifyApp).musicManager
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(NUM_PLAYS, numPlays)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->
            val song = args.getParcelable<Song>(ARG_SONG)
            if (song != null) {
                this.song = song
            }
            val listOfSongs = args.getParcelableArrayList<Song>(ARG_LIST)
            if (listOfSongs != null) {
                this.listOfSongs = listOfSongs
            }
        }
    }
    fun updateSong(song: Song) {
        this.song = song
        updateSongViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_song_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateSongViews()

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                numPlays = getInt(NUM_PLAYS, -1)
                tvNumberOfPlays.text = numPlays.toString()
            }
        }
        btnPlay.setOnClickListener {
            numPlays = numPlays.plus(1)
            tvNumberOfPlays.text = numPlays.toString()
        }

        btnNext.setOnClickListener {
            Toast.makeText(context, "Skipping to next track", Toast.LENGTH_SHORT).show()
            val randomSongIndex = Random.nextInt(0, listOfSongs.size - 1)
            musicManager.addToHistory(song)
            val nextSong = listOfSongs.get(randomSongIndex)
            updateSong(nextSong)
        }

        btnBack.setOnClickListener {
            if (musicManager.songHistorySize() > 0) {
                Toast.makeText(context, "Skipping to previous track", Toast.LENGTH_SHORT).show()
                val lastSong = musicManager.removeFromHistory()
                updateSong(lastSong)
                song = lastSong
            }
        }
    }

    private fun updateSongViews() {
        song?.let {
            tvTitle.text = song.title
            tvArtist.text = "${song.artist}"
            Picasso.get().load(song.largeImageURL).into(ivCover)
            tvNumberOfPlays.text = numPlays.toString()
        }
    }
}
