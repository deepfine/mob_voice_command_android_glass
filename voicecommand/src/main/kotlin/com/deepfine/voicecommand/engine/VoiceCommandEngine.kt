package com.deepfine.voicecommand.engine

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.deepfine.voicecommand.model.DeviceType
import com.deepfine.voicecommand.utils.DeviceTypeUtil

internal interface VoiceCommandEngine {
  val action: String
  val extraKey: String

  fun normalize(keyword: String): String

  fun normalize(keywords: Array<String>): String

  fun matches(keyword: String, command: String): Boolean

  companion object {
    fun get(activity: Activity?): VoiceCommandEngine? = when (DeviceTypeUtil.getDeviceType()) {
      DeviceType.VUZIX -> VuzixEngine(activity)
      DeviceType.REALWEAR -> RealWearEngine
      DeviceType.UNKNOWN -> null
    }
  }

  abstract class VoiceCommandReceiver(private val engine: VoiceCommandEngine?) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      if (intent.action == engine?.action) {
        val command = intent.getStringExtra(engine?.extraKey) ?: return
        onCommandReceive(command)
      }
    }

    abstract fun onCommandReceive(command: String)
  }
}

@Suppress("ParamsComparedByRef")
@Composable
internal fun rememberVoiceCommandEngine(
  activity: Activity?,
): VoiceCommandEngine? =
  remember {
    VoiceCommandEngine.get(activity)
  }
