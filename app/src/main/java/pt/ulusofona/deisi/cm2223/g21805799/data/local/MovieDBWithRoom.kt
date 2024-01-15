package pt.ulusofona.deisi.cm2223.g21805799.data.local

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g21805799.data.local.dao.MovieOperations
import pt.ulusofona.deisi.cm2223.g21805799.data.local.entities.Cinema
import pt.ulusofona.deisi.cm2223.g21805799.data.local.entities.MovieDB
import pt.ulusofona.deisi.cm2223.g21805799.model.DataManager
import pt.ulusofona.deisi.cm2223.g21805799.model.Movie
import java.lang.Double.parseDouble


class MovieDBWithRoom(private val storage: MovieOperations, val context: Context): DataManager() {


    override fun insertAllMovies(movies: List<Movie>, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            movies.map {
                MovieDB(
                    movieId = it.id,
                    name = it.name,
                    cinema = it.cinema,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    evaluation = it.evaluation,
                    dateWatched = it.dateWatched,
                    observations = it.observations,
                    watched = it.watched,
                    genre = it.genre,
                    poster = it.poster,
                    synopsis = it.synopsis,
                    datePremiere = it.datePremiere,
                    avgEvaluation = it.avgEvaluation,
                    link = it.link
                )
            }.forEach {
                storage.insert(it)
                Log.i("APP", "Inserted movie ${it.name} in DB")
            }
            onFinished()
        }

    }

    override fun insertMovie(movie: Movie, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val movieDb = MovieDB(
                movieId = movie.id,
                name = movie.name,
                cinema = movie.cinema,
                latitude = movie.latitude,
                longitude = movie.longitude,
                evaluation = movie.evaluation,
                dateWatched = movie.dateWatched,
                observations = movie.observations,
                watched = movie.watched,
                genre = movie.genre,
                poster = movie.poster,
                synopsis = movie.synopsis,
                datePremiere = movie.datePremiere,
                avgEvaluation = movie.avgEvaluation,
                link = movie.link
            )
            storage.insert(movieDb)
            Log.i("APP", "Manually Inserted movie:${movie.name} in DB")
            onFinished()
        }
    }

    override fun getAllMovies(onFinished: (List<Movie>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val movies = storage.getAll().map {
                Movie(
                    id = it.movieId,
                    name = it.name,
                    cinema = it.cinema,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    evaluation = it.evaluation,
                    dateWatched = it.dateWatched,
                    observations = it.observations,
                    watched = it.watched,
                    genre = it.genre,
                    poster = it.poster,
                    synopsis = it.synopsis,
                    datePremiere = it.datePremiere,
                    avgEvaluation = it.avgEvaluation,
                    link = it.link
                )
            }

            onFinished(movies)
        }
    }

    override fun getMovie(query: String, cinema: String, latitude: Double, longitude: Double, evaluation: Int, date: Long, observations: String, watched: Boolean, onFinished: (Result<Movie>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getMovieName(query: String, onFinished: (Result<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun clearAllMovies(onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            storage.deleteAll()
            onFinished()
        }
    }

    override fun getCinemas(onFinished: (List<Cinema>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {

            val inputStream = context.assets.open("cinemas.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            // Parse the JSON string into a JSON object
            val jsonObject = JSONObject(jsonString)

            // Now you can work with the JSON object
            val values = jsonObject.getJSONArray("cinemas")
            val cinemas = mutableListOf<Cinema>()
            for (i in 0 until values.length()) {
                val cinema = values.getJSONObject(i)
                cinemas.add(Cinema(cinema.get("cinema_name").toString(), parseDouble(cinema.get("latitude").toString()), parseDouble(cinema.get("longitude").toString())))
            }
            onFinished(cinemas)
        }
    }



}


