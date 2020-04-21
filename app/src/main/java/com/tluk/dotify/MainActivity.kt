package com.tluk.dotify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random
import android.view.View
import android.widget.Toast
import com.ericchee.songdataprovider.Song

class MainActivity : AppCompatActivity() {

    private val randomNumber = Random.nextInt(1000, 10000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val song = intent.getParcelableExtra<Song>(SONG_KEY)
        tvTitle.text = song.title
        tvArtist.text = "${song.artist}"
        ivCover.setImageResource(song.largeImageID)

        tvNumberOfPlays.text = randomNumber.toString()
        teUsername.visibility = View.INVISIBLE
    }

    fun changeUserClicked(view: View) {
        if (btnChangeUser.text == "change user") {
            teUsername.visibility = View.VISIBLE
            tvUsername.visibility = View.INVISIBLE
            teUsername.setText(tvUsername.text)
            btnChangeUser.text = "apply"
        } else {
            teUsername.visibility = View.INVISIBLE
            tvUsername.visibility = View.VISIBLE
            tvUsername.text = teUsername.text
            btnChangeUser.text = "change user"
        }
    }

    fun playClicked(view: View) {
        var currentNumberOfPlays: Int? = Integer.parseInt(tvNumberOfPlays.text.toString());
        currentNumberOfPlays = currentNumberOfPlays?.plus(1)
        tvNumberOfPlays.text = currentNumberOfPlays.toString()
    }
    fun nextClicked(view: View) {
        Toast.makeText(this, "Skipping to next track", Toast.LENGTH_SHORT).show()
    }

    fun backClicked(view: View) {
        Toast.makeText(this, "Skipping to previous track", Toast.LENGTH_SHORT).show()
    }

    companion object {
        // Keys for intents
        const val SONG_KEY = "SONG_KEY"
    }

}
