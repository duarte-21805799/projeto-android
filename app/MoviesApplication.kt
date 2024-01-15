package pt.ulusofona.deisi.cm2223.g21805799

import android.app.Application
import android.util.Log
import okhttp3.OkHttpClient
import pt.ulusofona.deisi.cm2223.g21805799.data.MoviesRepository
import pt.ulusofona.deisi.cm2223.g21805799.data.local.MovieDBWithRoom
import pt.ulusofona.deisi.cm2223.g21805799.data.local.MovieDatabase
import pt.ulusofona.deisi.cm2223.g21805799.data.remote.OMDBOkHttp
import pt.ulusofona.deisi.cm2223.g21805799.model.Movie

class MoviesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MoviesRepository.init(
            local = MovieDBWithRoom(MovieDatabase.getInstance(this).movieOperations(), this),
            remote = initOMDBOkHttp(),
            context = this
        )
        Log.i("APP", "Initialized repository")
    }

    private fun initOMDBOkHttp(): OMDBOkHttp {
        return OMDBOkHttp(
            "https://www.omdbapi.com/?apikey=24b67a84&t=",
            OkHttpClient()
        )
    }
}