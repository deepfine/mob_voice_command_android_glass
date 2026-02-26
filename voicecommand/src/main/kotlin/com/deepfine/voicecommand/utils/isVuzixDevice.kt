package com.deepfine.voicecommand.utils

import android.os.Build

fun isVuzixDevice(): Boolean {
  val manufacturer = Build.MANUFACTURER.lowercase()
  val model = Build.MODEL.lowercase()

  return manufacturer.contains("vuzix") || model.contains("vuzix")
}
