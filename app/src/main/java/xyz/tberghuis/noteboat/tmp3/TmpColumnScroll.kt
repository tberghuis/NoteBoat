package xyz.tberghuis.noteboat.tmp3

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TmpColumnScroll() {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier.verticalScroll(scrollState)
  ) {
    // Add your composables here. For example:
    repeat(100) {
      Text("Item $it", modifier = Modifier.padding(16.dp))
    }
  }
}