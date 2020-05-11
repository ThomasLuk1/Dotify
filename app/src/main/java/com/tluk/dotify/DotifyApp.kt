package com.tluk.dotify

import android.app.Application
import com.tluk.dotify.manager.MusicManager

class DotifyApp: Application() {
    lateinit var musicManager: MusicManager

    override fun onCreate() {
        super.onCreate()

        musicManager = MusicManager(this)
    }

}