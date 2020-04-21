package com.tluk.dotify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ericchee.songdataprovider.Song

class SongListAdapter(private val initialListOfSongs: List<Song>): RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {

    private var listOfSongs: List<Song> = initialListOfSongs.toList() // create a duplicate list of the list passed in
    var onSongClickListener: ((song: Song) -> Unit)? = null

    // Creates ViewHolder to hold reference of the views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    //Size of items to load
    override fun getItemCount() = listOfSongs.size

    // Sets data on view
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song:Song = listOfSongs[position]
        holder.bind(song)
    }

    fun change(newSongs: List<Song>) {
        listOfSongs = newSongs
        notifyDataSetChanged()
    }

    inner class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvSongName = itemView.findViewById<TextView>(R.id.tvSongName)
        private val ivSongImage = itemView.findViewById<ImageView>(R.id.ivSongImage)
        private val tvSongArtist = itemView.findViewById<TextView>(R.id.tvSongArtist)

        fun bind(song: Song) {
            tvSongName.text = song.title
            tvSongArtist.text = song.artist
            ivSongImage.setImageResource(song.smallImageID)

            itemView.setOnClickListener {
                onSongClickListener?.invoke(song)
            }
        }
    }

}