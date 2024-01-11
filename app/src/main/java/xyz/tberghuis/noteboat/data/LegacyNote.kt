package xyz.tberghuis.noteboat.data

import androidx.room.*

// this comes from app_flutter/note.db
@Entity(
  tableName = "note",
)
data class LegacyNote(
  @PrimaryKey
  @ColumnInfo(name = "note_id")
  val noteId: Int?,
  @ColumnInfo(name = "note_text") val noteText: String?,
  @ColumnInfo(name = "modified_date") val modifiedDate: String?,
  @ColumnInfo(name = "created_date") val createdDate: String?,
)

@Dao
interface LegacyNoteDao {
  @Query("SELECT * FROM note")
  fun getAll(): List<LegacyNote>
}

@Database(
  entities = [LegacyNote::class],
  version = 1,
)
abstract class LegacyDatabase : RoomDatabase() {
  abstract fun legacyNoteDao(): LegacyNoteDao
}