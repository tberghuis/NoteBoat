package xyz.tberghuis.noteboat.tmp.tmp01

class TmpRepo {
// todo val notes MSF

}


data class TmpNote(
  val noteId: Int,
  val noteText: String,
  val categoryId: Int,
)

data class TmpCategory(
  val categoryId: Int,
  val categoryText: String,
  val color: String
)
