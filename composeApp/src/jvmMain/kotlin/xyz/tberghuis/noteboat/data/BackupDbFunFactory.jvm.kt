package xyz.tberghuis.noteboat.data

import androidx.room3.useWriterConnection
import java.io.File
import xyz.tberghuis.noteboat.DB_FILENAME
import xyz.tberghuis.noteboat.utils.logd

actual class BackupDbFunFactory(
  private val db: AppDatabase,
) {
  actual fun create(): suspend (backupFileUri: String) -> Unit {
    return { backupFileUri ->
      // checkpoint
      db.useWriterConnection { transactor ->
        transactor.usePrepared("pragma wal_checkpoint(full)") { stmt ->
          if (stmt.step()) {
            val busy = stmt.getLong(0)
            val log = stmt.getLong(1)
            val checkpointed = stmt.getLong(2)
            logd("backupDb busy $busy log $log checkpointed $checkpointed")
          }
        }
      }
      val dbFile = File(System.getProperty("java.io.tmpdir"), DB_FILENAME)
      dbFile.copyTo(
        target = File(backupFileUri),
        overwrite = true,
      )

    }
  }
}