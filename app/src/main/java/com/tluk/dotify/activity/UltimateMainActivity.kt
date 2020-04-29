package com.tluk.dotify.activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ericchee.songdataprovider.Song
import com.ericchee.songdataprovider.SongDataProvider
import com.tluk.dotify.fragment.NowPlayingFragment
import com.tluk.dotify.R
import com.tluk.dotify.fragment.SongListFragment
import kotlinx.android.synthetic.main.activity_ultimate_main.*

class UltimateMainActivity : AppCompatActivity(), OnSongClickListener {
    private var song: Song? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ultimate_main)

        if (getNowPlayingFragment() == null) {
            val songListFragment = SongListFragment()
            val listOfSongs: ArrayList<Song> = SongDataProvider.getAllSongs() as ArrayList<Song>
            val argumentBundle = Bundle().apply {
                putParcelableArrayList(SongListFragment.ARG_LIST, listOfSongs)
            }
            songListFragment.arguments = argumentBundle

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragContainer, songListFragment)
                .commit()
        }
        supportFragmentManager.addOnBackStackChangedListener {
            val hasBackStack = supportFragmentManager.backStackEntryCount > 0
            if (hasBackStack) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
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

        btnShuffle.setOnClickListener {
            var songListFragment = SongListFragment()
            songListFragment.shuffleList()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return super.onNavigateUp()
    }

    private fun getNowPlayingFragment() = supportFragmentManager.findFragmentByTag(NowPlayingFragment.TAG) as? NowPlayingFragment

    override fun onSongClicked(song: Song) {
        tvCurrentSong.text = "${song.title} - ${song.artist}"
        this.song = song
    }

}

interface OnSongClickListener {
    fun onSongClicked(song: Song)
}
