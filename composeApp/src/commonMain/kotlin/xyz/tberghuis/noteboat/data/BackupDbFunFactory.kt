package xyz.tberghuis.noteboat.data

expect class BackupDbFunFactory {
  fun create(): suspend (backupFileUri: String) -> Unit
}