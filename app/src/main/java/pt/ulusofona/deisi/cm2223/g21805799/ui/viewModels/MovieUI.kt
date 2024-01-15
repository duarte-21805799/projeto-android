package pt.ulusofona.deisi.cm2223.g21805799.ui.viewModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieUI(
    val id: String,
    val name: String,
    val cinema: String,
    val latitude: Double,
    val longitude: Double,
    val evaluation: Int,
    val dateWatched: Long,
    val observations: String,
    val watched: Boolean,
    val genre: String,
    val poster: String,
    val synopsis: String,
    val datePremiere: String,
    val avgEvaluation: Double,
    val link: String,
    val distanceFromMe : Int
) : Parcelable
