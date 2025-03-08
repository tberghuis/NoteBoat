package xyz.tberghuis.noteboat.tmp.tmp00

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.noteboat.tmp.tmp01.TmpVm
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme

@Composable
fun TmpScreen(
  vm: TmpVm = viewModel()
) {
  Column {
    Text("hello tmp screen")
    Text("category list: ${vm.categoryList}")

    Button(onClick = {
      vm.insertCategories()
    }) {
      Text("insertCategories")
    }

  }
}

@Preview
@Composable
fun TmpScreenPreview() {
  NoteBoatTheme {
    Surface(
      modifier = Modifier.fillMaxSize(),
    ) {
      TmpScreen()
    }
  }
}