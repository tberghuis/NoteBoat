package xyz.tberghuis.noteboat.utils

import android.util.Log
//import xyz.tberghuis.noteboat.BuildConfig

//fun logd(s: String) {
//  if (BuildConfig.DEBUG) Log.d("xxx", s)
//}

// todo test that proguard removes this from release builds
fun logd(s: String) {
  Log.d("xxx", s)
}