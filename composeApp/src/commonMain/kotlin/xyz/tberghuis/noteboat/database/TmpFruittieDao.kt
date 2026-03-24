package xyz.tberghuis.noteboat.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import xyz.tberghuis.noteboat.data.Fruittie

@Dao
interface FruittieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fruittie: Fruittie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fruitties: List<Fruittie>)

    @Query("SELECT * FROM Fruittie")
    fun getAllAsFlow(): Flow<List<Fruittie>>

    @Query("SELECT COUNT(*) as count FROM Fruittie")
    suspend fun count(): Int

    @Query("SELECT * FROM Fruittie WHERE id = :id")
    suspend fun getFruittie(id: Long): Fruittie?

    @Query("SELECT * FROM Fruittie WHERE id in (:ids)")
    suspend fun loadAll(ids: List<Long>): List<Fruittie>

    @Query("SELECT * FROM Fruittie WHERE id in (:ids)")
    suspend fun loadMapped(ids: List<Long>): Map<
        @MapColumn(columnName = "id")
        Long,
        Fruittie,
    >
}
