package pt.ulusofona.deisi.cm2223.g21805799.model

import pt.ulusofona.deisi.cm2223.g21805799.data.local.entities.Cinema

abstract class DataManager() {

    abstract fun getAllMovies(onFinished: (List<Movie>) -> Unit)
    abstract fun getMovie(query: String, cinema: String, latitude: Double, longitude: Double, evaluation: Int, date: Long, observations: String, watched: Boolean, onFinished: (Result<Movie>) -> Unit)
    abstract fun getMovieName(query: String, onFinished: (Result<String>) -> Unit)
    abstract fun insertAllMovies(movies: List<Movie>, onFinished: () -> Unit)
    abstract fun clearAllMovies(onFinished: () -> Unit)
    abstract fun insertMovie(movie: Movie, onFinished: () -> Unit)
    abstract fun getCinemas(onFinished: (List<Cinema>) -> Unit)


}
