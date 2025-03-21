package xyz.tberghuis.noteboat.tmp.tmp02

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import xyz.tberghuis.noteboat.utils.logd

@Preview
@Composable
fun FfiTest() {
  Column() {
    Button(onClick = {
      logd("test")
    }) {
      Text("test")
    }
  }
}