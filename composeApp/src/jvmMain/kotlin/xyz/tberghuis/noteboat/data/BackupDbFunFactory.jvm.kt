package xyz.tberghuis.noteboat.data

actual class BackupDbFunFactory {
  actual fun create(): suspend (backupFileUri: String) -> Unit {
    TODO("Not yet implemented")
  }
}