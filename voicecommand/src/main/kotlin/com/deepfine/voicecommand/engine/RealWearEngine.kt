package com.deepfine.voicecommand.engine

import com.deepfine.voicecommand.utils.RealWear

object RealWearEngine : VoiceCommandEngine {
  override val action = RealWear.ACTION_SPEECH_EVENT
  override val extraKey = RealWear.EXTRA_SPEECH_COMMAND

  override fun normalize(keyword: String) = RealWear.normalizeVoiceCommand(keyword)

  override fun matches(keyword: String, command: String) = RealWear.matchesVoiceCommand(keyword, command)
}
