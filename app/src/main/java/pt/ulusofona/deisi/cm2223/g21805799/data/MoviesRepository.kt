package pt.ulusofona.deisi.cm2223.g21805799.data

import android.content.Context
import android.util.Log
import pt.ulusofona.deisi.cm2223.g21805799.data.local.entities.Cinema
import pt.ulusofona.deisi.cm2223.g21805799.data.remote.ConnectivityUtil
import pt.ulusofona.deisi.cm2223.g21805799.model.Movie
import pt.ulusofona.deisi.cm2223.g21805799.model.DataManager
import java.lang.IllegalStateException

class MoviesRepository private constructor(val local: DataManager, val remote: DataManager, val context: Context): DataManager() {

    override fun getAllMovies(onFinished: (List<Movie>) -> Unit) {
        local.getAllMovies(onFinished)
    }

    override fun getMovieName(query: String, onFinished: (Result<String>) -> Unit) {
        if (ConnectivityUtil.isOnline(context)) {
            // Se tenho acesso à Internet, vou buscar os registos ao web service
            remote.getMovieName(query) { result ->
                if (result.isSuccess) {
                    result.getOrNull()
                        ?.let { movie ->
                            onFinished(Result.success(movie))
                        }
                } else {
                    Log.w("APP", "Error getting movie from server")
                    onFinished(result)  // propagate the remote failure
                }
            }

            // Continua no próximo quadro
        } else {
            // O que fazer se não houver Internet?
            Log.i("APP", "App is offline.")
        }
    }

    override fun getMovie(query: String, cinema: String, latitude: Double, longitude: Double, evaluation: Int, date: Long, observations: String, watched: Boolean, onFinished: (Result<Movie>) -> Unit) {
        if (ConnectivityUtil.isOnline(context)) {
            // Se tenho acesso à Internet, vou buscar os registos ao web service
            remote.getMovie(query, cinema, latitude, longitude, evaluation, date, observations, watched) { result ->
                if (result.isSuccess) {
                    result.getOrNull()
                        ?.let { movie ->
                            insertMovie(movie) {
                            }
                            onFinished(Result.success(movie))
                        }
                } else {
                    Log.w("APP", "Error getting movie from server")
                    onFinished(result)  // propagate the remote failure
                }
            }

            // Continua no próximo quadro
        } else {
            // O que fazer se não houver Internet?
            Log.i("APP", "App is offline.")
        }
    }


    override fun insertMovie(movie: Movie, onFinished: () -> Unit) {
        local.insertMovie(movie) {
            onFinished()
        }
    }

    override fun insertAllMovies(movies: List<Movie>, onFinished: () -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun clearAllMovies(onFinished: () -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun getCinemas(onFinished: (List<Cinema>) -> Unit) {
        local.getCinemas {
            onFinished(it)
        }
    }

    companion object {
        private var instance: MoviesRepository? = null

        fun init(local: DataManager, remote: DataManager, context: Context) {
            synchronized(this) {
                if (instance == null) {
                    instance = MoviesRepository(local, remote, context)
                }
            }
        }

        fun getInstance(): MoviesRepository {
            if (instance == null) {
                throw IllegalStateException("singleton not initialized")
            }
            return instance as MoviesRepository

        }
    }
}