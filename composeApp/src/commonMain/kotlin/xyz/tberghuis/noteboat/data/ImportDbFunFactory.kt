package xyz.tberghuis.noteboat.data

expect class ImportDbFunFactory {
  fun create(): suspend (importDbUri: String) -> Unit
}