package pt.ulusofona.deisi.cm2223.g21805799.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieDB(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        @ColumnInfo(name="movie_id")
        val movieId: String,  // movieId corresponds to MovieUi.id
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


        )

