package xyz.tberghuis.noteboat.tmp2

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme
import xyz.tberghuis.noteboat.utils.logd

@Composable
fun TmpImportDbScreen() {
  Column {
    Text("import db")
    DeleteAllNotes()
    ImportDb()
  }
}

@Preview
@Composable
fun TmpImportDbScreenPreview() {
  NoteBoatTheme {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
      TmpImportDbScreen()
    }
  }
}

@Composable
fun ImportDb(
  vm: TmpImportDbVm = viewModel()
) {
  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      logd("rememberLauncherForActivityResult $result")
      when (result.resultCode) {
        RESULT_OK -> {
          result.data?.data?.let { vm.importDb(it) }
        }
      }
    }

  Button(onClick = {
    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
      type = "*/*"
      addCategory(Intent.CATEGORY_OPENABLE)
    }
    launcher.launch(intent)
  }) {
    Text("ImportDb")
  }
}

@Composable
fun DeleteAllNotes(
  vm: TmpImportDbVm = viewModel()
) {
  Button(onClick = { vm.deleteAllNotes() }) {
    Text("DeleteAllNotes")
  }
}