package xyz.tberghuis.noteboat.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import kotlinx.datetime.*


// should have put this file in tmp folder

@Composable
fun DateTimeDemo() {

  Column {
    Text("hello date time demo")
    Button(onClick = {
      val currentMoment = Clock.System.now()
      val epoch = currentMoment.toEpochMilliseconds()

      val moment2 = Instant.fromEpochMilliseconds(epoch)

//      val m3 = Instant.parse("2022-02-10T17:38:09.229050")
//      val localDateTime = m3.toLocalDateTime(TimeZone.currentSystemDefault())
      val localDateTime = "2022-02-10T17:38:09.229050".toLocalDateTime()

      localDateTime.toJavaLocalDateTime() // https://stackoverflow.com/questions/22463062/how-to-parse-format-dates-with-localdatetime-java-8
      val epoch2 = localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
      Log.d("xxx", "currentMoment $currentMoment $localDateTime $epoch2")
    }) {
      Text("button")
    }
  }

}