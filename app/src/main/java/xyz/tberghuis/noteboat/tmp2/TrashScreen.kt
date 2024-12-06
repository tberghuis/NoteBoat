package xyz.tberghuis.noteboat.tmp2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import xyz.tberghuis.noteboat.screen.HomeContent
import xyz.tberghuis.noteboat.screen.HomeTopBar

@Composable
fun TrashScreen() {
  Scaffold(
    topBar = { HomeTopBar() },
    content = { contentPadding ->
      Box(
        Modifier
          .padding(contentPadding)
          .navigationBarsPadding()
      ) {
        HomeContent()
      }
    },
  )
}