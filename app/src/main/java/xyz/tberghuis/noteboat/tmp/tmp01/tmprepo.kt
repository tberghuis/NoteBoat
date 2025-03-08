package xyz.tberghuis.noteboat.tmp.tmp01

import androidx.room.Entity
import androidx.room.PrimaryKey

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