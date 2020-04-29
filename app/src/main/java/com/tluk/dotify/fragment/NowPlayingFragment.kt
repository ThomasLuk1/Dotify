package com.tluk.dotify.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ericchee.songdataprovider.Song
import com.tluk.dotify.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnBack
import kotlinx.android.synthetic.main.activity_main.btnNext
import kotlinx.android.synthetic.main.activity_main.btnPlay
import kotlinx.android.synthetic.main.activity_main.ivCover
import kotlinx.android.synthetic.main.activity_main.tvArtist
import kotlinx.android.synthetic.main.activity_main.tvNumberOfPlays
import kotlinx.android.synthetic.main.activity_main.tvTitle
import kotlinx.android.synthetic.main.activity_ultimate_main.*
import kotlinx.android.synthetic.main.fragment_song_detail.*
import kotlin.random.Random

class NowPlayingFragment : Fragment() {
    private lateinit var song: Song
    private var numPlays = Random.nextInt(1000, 10000)

    companion object {
        const val ARG_SONG = "ARG_SONG"
        val TAG:String = NowPlayingFragment::class.java.simpleName
        const val NUM_PLAYS = "num_plays"
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
        btnPlay.setOnClickListener { btnPlay: View ->
            numPlays = numPlays.plus(1)
            tvNumberOfPlays.text = numPlays.toString()
        }

        btnNext.setOnClickListener { btnNext: View ->
            Toast.makeText(context, "Skipping to next track", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener { btnNext: View ->
            Toast.makeText(context, "Skipping to previous track", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateSongViews() {
        song?.let {
            tvTitle.text = song.title
            tvArtist.text = "${song.artist}"
            ivCover.setImageResource(song.largeImageID)
            tvNumberOfPlays.text = numPlays.toString()
        }
    }
}
