package com.tluk.dotify.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tluk.dotify.model.Song
import com.tluk.dotify.DotifyApp
import com.tluk.dotify.R
import com.tluk.dotify.SongListAdapter
import com.tluk.dotify.activity.OnSongClickListener
import com.tluk.dotify.manager.MusicManager
import kotlinx.android.synthetic.main.activity_song_list.*
import kotlinx.android.synthetic.main.activity_song_list.rvSongList
import kotlinx.android.synthetic.main.activity_ultimate_main.*
import kotlinx.android.synthetic.main.fragment_list_songs.*

class SongListFragment : Fragment(){

    private lateinit var songAdapter: SongListAdapter
    private var onSongClickListener: OnSongClickListener? = null
    private lateinit var listOfSongs: MutableList<Song>
    private lateinit var musicManager: MusicManager

    companion object {
        val TAG = SongListFragment::class.java.simpleName
        const val ARG_LIST = "ARG_LIST"
        const val ARG_SHUFFLED_LIST = "ARG_SHUFFLED_LIST"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnSongClickListener) {
            onSongClickListener = context
        }

        musicManager = (context.applicationContext as DotifyApp).musicManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                val oldList = getParcelableArrayList<Song>(ARG_SHUFFLED_LIST)?.toMutableList()
                if (oldList != null) {
                    listOfSongs = oldList
                    Log.i("info", listOfSongs.toString())
                }
            }
        } else {
            Log.i("info", "no save")
            arguments?.let { args ->
                val listOfSongs = args.getParcelableArrayList<Song>(ARG_LIST)
                if (listOfSongs != null) {
                    this.listOfSongs = listOfSongs.toMutableList()
                }
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

        rvSongList.adapter = songAdapter

        songAdapter.onSongClickListener = { song ->
            onSongClickListener?.onSongClicked(song)
        }

        swipeRefresh.setOnRefreshListener {
            fetchSongs()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList(ARG_SHUFFLED_LIST, ArrayList(listOfSongs))

    }

    fun shuffleList() {
        listOfSongs = listOfSongs.apply {
            shuffle()
        }
        songAdapter.change(listOfSongs)
        rvSongList.adapter = songAdapter
    }

    fun fetchSongs() {
        musicManager.getMusicLibrary({ musicLibrary ->
            Log.i("info", "There are ${musicLibrary.numOfSongs} songs.")
            listOfSongs = musicLibrary.songs.toMutableList()
            songAdapter.change(listOfSongs)
            rvSongList.adapter = songAdapter
        }, {
            Log.i("info", "error fetching songs")
        })
        swipeRefresh.isRefreshing = false
    }
}

