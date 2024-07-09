package xyz.tberghuis.noteboat.tmp2

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme

@Preview
@Composable
fun TmpImportDbScreenPreview() {
  NoteBoatTheme {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
//      TmpSettingsContent(PaddingValues())
    }
  }
}