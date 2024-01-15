package pt.ulusofona.deisi.cm2223.g21805799.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.ulusofona.deisi.cm2223.g21805799.data.local.entities.MovieDB

@Dao
interface MovieOperations {

    @Insert
    fun insert(operation: MovieDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(operations: List<MovieDB>)

    @Query("SELECT * FROM moviedb")
    fun getAll(): List<MovieDB>

    @Query("DELETE FROM moviedb")
    fun deleteAll()

}