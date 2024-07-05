package xyz.tberghuis.noteboat.tmp2

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

@Composable
fun TmpImportDbScreen(
  vm: TmpImportDbVm = viewModel()
) {
  Column {
    Text("import db")
    Button(onClick = { vm.filePicker() }) {
      Text("file picker")
    }
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