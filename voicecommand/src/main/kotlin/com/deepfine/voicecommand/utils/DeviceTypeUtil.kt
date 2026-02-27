package com.deepfine.voicecommand.utils

import android.os.Build
import com.deepfine.voicecommand.model.DeviceType

internal object DeviceTypeUtil {
  val deviceModel = Build.MODEL.uppercase()

  private val vuzixModels = setOf("Blade 2", "M400", "M4000")
  private val realWearModels = setOf("T1100G", "T1200G", "T21G", "A31G")

  private val isVuzix: Boolean = vuzixModels.any { deviceModel.contains(it) }

  private val isRealWear: Boolean = realWearModels.any { deviceModel.contains(it) }

  fun getDeviceType(): DeviceType = when {
    isVuzix -> DeviceType.VUZIX
    isRealWear -> DeviceType.REALWEAR
    else -> DeviceType.UNKNOWN
  }
}
