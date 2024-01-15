package pt.ulusofona.deisi.cm2223.g21805799.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ulusofona.deisi.cm2223.g21805799.data.local.dao.MovieOperations
import pt.ulusofona.deisi.cm2223.g21805799.data.local.entities.MovieDB

@Database(entities = [MovieDB::class], version = 5)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieOperations(): MovieOperations


    companion object {

        private var instance: MovieDatabase? = null

        fun getInstance(applicationContext: Context): MovieDatabase {
            synchronized(this) {
                if(instance == null) {
                    instance = Room.databaseBuilder(
                        applicationContext,
                        MovieDatabase::class.java,
                        "movie_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance as MovieDatabase
            }
        }
    }
}