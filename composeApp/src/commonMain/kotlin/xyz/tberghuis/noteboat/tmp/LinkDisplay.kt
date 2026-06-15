package xyz.tberghuis.noteboat.tmp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import sh.calvin.autolinktext.rememberAutoLinkText

@Composable
fun LinkDisplay() {

  Card(
    modifier = Modifier.fillMaxWidth()
//      .combinedClickable()
      
      .clickable {
      println("card clicked")

    },
  ) {
    Text(
      AnnotatedString.rememberAutoLinkText(
        """
        |Visit https://www.google.com
        |Visit www.google.com
        |Email test@example.com
        |Call 6045557890
        |Call +1 (604) 555-7890
        |Call 604-555-7890
        |hello.com
        """.trimMargin()
      )
    )

  }


}