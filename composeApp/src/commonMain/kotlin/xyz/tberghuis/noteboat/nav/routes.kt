package xyz.tberghuis.noteboat.nav

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Serializable
data object RouteHome : NavKey

@Serializable
data class RouteNewNote(val voice: Boolean = false) : NavKey

@Serializable
data class RouteEditNote(val noteId: Int) : NavKey

@Serializable
data object RouteSettings : NavKey

@Serializable
data object RouteTrash : NavKey

val navSerializationModule = SerializersModule {
  polymorphic(NavKey::class) {
    subclass(RouteHome::class)
    subclass(RouteNewNote::class)
    subclass(RouteEditNote::class)
    subclass(RouteSettings::class)
    subclass(RouteTrash::class)
  }
}

val LocalBackStackState = compositionLocalOf<NavBackStack<NavKey>> {
  error("CompositionLocal LocalBackStackState not present")
}