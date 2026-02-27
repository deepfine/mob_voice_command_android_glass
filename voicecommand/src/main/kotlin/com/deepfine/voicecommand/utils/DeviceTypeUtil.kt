package com.deepfine.voicecommand.utils

import android.os.Build
import com.deepfine.voicecommand.model.DeviceType

internal object DeviceTypeUtil {
  val deviceModel = Build.MODEL.uppercase()

  private val vuzixModels = listOf("M400", "M4000", "Blade 2")
  private val realWearModels = listOf("T21G", "T21G", "T120", "A31G")

  fun isVuzix(): Boolean = vuzixModels.any { deviceModel.contains(it) }

  fun isRealWear(): Boolean = realWearModels.any { deviceModel.contains(it) }

  fun getDeviceType(): DeviceType {
    if (isVuzix()) {
      return DeviceType.VUZIX
    } else if (isRealWear()) {
      return DeviceType.REALWEAR
    } else {
      return DeviceType.UNKNOWN
    }
  }
}
