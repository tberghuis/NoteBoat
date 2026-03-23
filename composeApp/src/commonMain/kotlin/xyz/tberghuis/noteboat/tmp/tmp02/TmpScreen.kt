package xyz.tberghuis.noteboat.tmp.tmp02

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import xyz.tberghuis.noteboat.tmp.tmp03.TmpKoinVm

@Composable
fun TmpScreen(
) {

  val viewModel = koinViewModel<TmpKoinVm>()

  Column {
    Text("will: ${viewModel.willitblend}")
    Text("hello world screen")

    Button(onClick = {
    }) {
      Text("insertCategories")
    }

  }
}

@Preview
@Composable
fun TmpScreenPreview() {
  MaterialTheme {
    Surface(
      modifier = Modifier.fillMaxSize(),
    ) {
      TmpScreen()
    }
  }
}