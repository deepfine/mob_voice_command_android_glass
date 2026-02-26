package com.deepfine.voicecommand.engine

import androidx.compose.runtime.compositionLocalOf

val LocalVoiceEngine = compositionLocalOf<VoiceCommandEngine> {
  error("VoiceCommandEngine not provided")
}
