package xyz.tberghuis.noteboat.data

import androidx.annotation.NonNull
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(
  tableName = "note",
)
data class Note(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "note_id")
  val noteId: Int = 0,
  @NonNull @ColumnInfo(name = "note_text") val noteText: String,

  // epoch = Clock.System.now().toEpochMilliseconds()
  // moment = Instant.fromEpochMilliseconds(epoch)
  // date = moment.toLocalDateTime(TimeZone.currentSystemDefault())
  @NonNull @ColumnInfo(name = "created_epoch") val createdEpoch: Long,
  @NonNull @ColumnInfo(name = "modified_epoch") val modifiedEpoch: Long
)

@Dao
interface NoteDao {
  @Query("SELECT * FROM note ORDER BY modified_epoch DESC")
  fun getAll(): Flow<List<Note>>

  @Query("SELECT * FROM note where note_id = :noteId")
  fun getNote(noteId: Int): Flow<Note?>

  @Query("SELECT note_text FROM note where note_id = :noteId")
  fun getNoteText(noteId: Int): String

  @Insert
  suspend fun insertAll(vararg note: Note)

  @Delete
  suspend fun delete(vararg note: Note)

  @Query("delete from note where note_id = :noteId")
  suspend fun delete(noteId: Int)

  @Query("UPDATE note set note_text = :noteText, modified_epoch = :modifiedEpoch WHERE note_id = :noteId")
  suspend fun updateNoteText(noteId: Int, noteText: String, modifiedEpoch: Long)
}