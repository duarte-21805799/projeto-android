package pt.ulusofona.deisi.cm2223.g21805799.ui.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.cm2223.g21805799.data.MoviesRepository
import pt.ulusofona.deisi.cm2223.g21805799.model.DataManager

class MoviesViewModel(application: Application): AndroidViewModel(application) {

    private val model: DataManager = MoviesRepository.getInstance()


    fun getDashboardInfo(onFinished: (Array<String>) -> Unit) {
        var responseArray = arrayOf("0","5","10","20")
        onFinished(responseArray)
    }


    fun getAllMovies(onFinished: (ArrayList<MovieUI>) -> Unit) {
        // transforms "pure" movie into parcelable MovieUI so it can be displayed on RecyclerView List
        model.getAllMovies {
            Log.i("APP", "Received ${it.size} movies from WS")
            val moviesUI = ArrayList(it.map { movie ->
                MovieUI(
                    movie.id,
                    movie.name,
                    movie.cinema,
                    movie.latitude,
                    movie.longitude,
                    movie.evaluation,
                    movie.dateWatched,
                    movie.observations,
                    movie.watched,
                    movie.genre,
                    movie.poster,
                    movie.synopsis,
                    movie.datePremiere,
                    movie.avgEvaluation,
                    movie.link,
                    999999
                )
            })
            onFinished(moviesUI)
        }
    }
}