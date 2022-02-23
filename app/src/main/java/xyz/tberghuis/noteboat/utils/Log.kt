package xyz.tberghuis.noteboat.utils

import android.util.Log
import xyz.tberghuis.noteboat.BuildConfig

fun logd(s: String) {
  if (BuildConfig.DEBUG) Log.d("xxx", s)
}