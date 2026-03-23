package xyz.tberghuis.noteboat.utils

import android.util.Log

// todo test that proguard removes this from release builds
actual fun logd(s: String) {
    Log.d("xxx", s)
}