package xyz.tberghuis.noteboat.tmp.tmp01

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import androidx.room.Delete
import androidx.room.Update

@Entity
data class TmpCategory(
  @PrimaryKey val categoryId: Int,
  val categoryText: String,
  val color: String
)

@Entity
data class TmpNote(
  @PrimaryKey val noteId: Int,
  val noteText: String,
  val categoryId: Int,
)

@Dao
interface TmpCategoryDao {
  @Query("SELECT * FROM TmpCategory")
  fun getAll(): Flow<List<TmpCategory>>

  @Insert
  suspend fun insertAll(vararg category: TmpCategory)

  @Delete
  suspend fun delete(vararg category: TmpCategory)

  @Update
  suspend fun update(category: TmpCategory)
}