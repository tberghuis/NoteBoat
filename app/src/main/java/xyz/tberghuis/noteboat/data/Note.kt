package xyz.tberghuis.noteboat.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(
  tableName = "note",
)
data class Note(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "note_id")
  val noteId: Int = 0,
  @ColumnInfo(name = "note_text") val noteText: String,

  // epoch = Clock.System.now().toEpochMilliseconds()
  // moment = Instant.fromEpochMilliseconds(epoch)
  // date = moment.toLocalDateTime(TimeZone.currentSystemDefault())
  @ColumnInfo(name = "created_epoch") val createdEpoch: Long,
  @ColumnInfo(name = "modified_epoch") val modifiedEpoch: Long,

  @ColumnInfo(name = "pinned", defaultValue = "0") val pinned: Boolean = false,
)

@Dao
interface NoteDao {
  @Query("SELECT * FROM note ORDER BY pinned DESC, modified_epoch DESC")
  fun getAll(): Flow<List<Note>>

  @Query("SELECT * FROM note where note_id = :noteId")
  fun getNote(noteId: Int): Flow<Note?>

  @Query("SELECT note_text FROM note where note_id = :noteId")
  suspend fun getNoteText(noteId: Int): String

  @Insert
  suspend fun insertAll(vararg note: Note)

  suspend fun insertAll(notes: List<Note>) {
    insertAll(*notes.toTypedArray())
  }

  @Delete
  suspend fun delete(vararg note: Note)

  @Query("delete from note where note_id = :noteId")
  suspend fun delete(noteId: Int)

  @Query("delete from note")
  suspend fun deleteAll()

  @Query("UPDATE note set note_text = :noteText, modified_epoch = :modifiedEpoch WHERE note_id = :noteId")
  suspend fun updateNoteText(noteId: Int, noteText: String, modifiedEpoch: Long)

  @Update
  suspend fun update(note: Note)
}