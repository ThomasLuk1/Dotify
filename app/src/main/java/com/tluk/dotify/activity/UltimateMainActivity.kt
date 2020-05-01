package com.tluk.dotify.activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ericchee.songdataprovider.Song
import com.ericchee.songdataprovider.SongDataProvider
import com.tluk.dotify.fragment.NowPlayingFragment
import com.tluk.dotify.R
import com.tluk.dotify.fragment.SongListFragment
import kotlinx.android.synthetic.main.activity_ultimate_main.*

class UltimateMainActivity : AppCompatActivity(), OnSongClickListener {
    private var song: Song? = null

    companion object {
        const val CURR_SONG = "CURR_SONG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ultimate_main)
        val listOfSongs: ArrayList<Song> = ArrayList(SongDataProvider.getAllSongs())

        var hasBackStack = supportFragmentManager.backStackEntryCount > 0
        if (hasBackStack) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            nowPlayingBar.visibility = View.GONE
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            nowPlayingBar.visibility = View.VISIBLE
        }
        supportFragmentManager.addOnBackStackChangedListener {
            hasBackStack = supportFragmentManager.backStackEntryCount > 0
            if (hasBackStack) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                nowPlayingBar.visibility = View.GONE
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                nowPlayingBar.visibility = View.VISIBLE
            }
        }


        if (supportFragmentManager.findFragmentByTag(NowPlayingFragment.TAG) == null) {
            val argumentBundle = Bundle().apply {
                putParcelableArrayList(SongListFragment.ARG_LIST, listOfSongs)
            }
            val songListFragment = SongListFragment()
            songListFragment.arguments = argumentBundle
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragContainer, songListFragment, SongListFragment.TAG)
                .commit()
        }

        btnShuffle.setOnClickListener {
            val songListFragment = getSongListFragment()
            if (songListFragment != null) {
                songListFragment.shuffleList()
            }
        }

        nowPlayingBar.setOnClickListener {
            val currSong = song
            if (currSong != null) {
                var nowPlayingFragment = getNowPlayingFragment()
                if  (nowPlayingFragment == null) {
                    nowPlayingFragment = NowPlayingFragment()
                    val argumentBundle = Bundle().apply {
                        putParcelable(NowPlayingFragment.ARG_SONG, currSong)
                    }
                    nowPlayingFragment.arguments = argumentBundle
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.fragContainer, nowPlayingFragment, NowPlayingFragment.TAG)
                        .addToBackStack(NowPlayingFragment.TAG)
                        .commit()
                } else {
                    nowPlayingFragment.updateSong(currSong)
                }
            }
        }
        if (savedInstanceState != null) {
            with(savedInstanceState) {
                tvCurrentSong.text = getString(CURR_SONG)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return super.onNavigateUp()
    }

    private fun getSongListFragment() = supportFragmentManager.findFragmentByTag(SongListFragment.TAG) as? SongListFragment

    private fun getNowPlayingFragment() = supportFragmentManager.findFragmentByTag(NowPlayingFragment.TAG) as? NowPlayingFragment

    override fun onSongClicked(song: Song) {
        tvCurrentSong.text = "${song.title} - ${song.artist}"
        this.song = song
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(CURR_SONG, tvCurrentSong.text.toString())
    }
}

interface OnSongClickListener {
    fun onSongClicked(song: Song)
}
