package pt.ulusofona.deisi.cm2223.g21805799.model

import java.util.*

data class Movie(
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
    val link: String
) {

   val timestamp: Long = Date().time

}
