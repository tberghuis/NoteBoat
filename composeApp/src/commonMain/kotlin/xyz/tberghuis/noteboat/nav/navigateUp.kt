package xyz.tberghuis.noteboat.nav

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateUp() {
  if (this.size > 1) {
    this.removeLastOrNull()
  }
}