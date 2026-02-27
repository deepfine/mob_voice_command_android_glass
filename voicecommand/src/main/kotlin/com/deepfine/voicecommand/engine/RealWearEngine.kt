package com.deepfine.voicecommand.engine

import com.deepfine.voicecommand.utils.RealWear

internal object RealWearEngine : VoiceCommandEngine {
  override val action = RealWear.ACTION_SPEECH_EVENT
  override val extraKey = RealWear.EXTRA_SPEECH_COMMAND

  override fun normalize(keyword: String) = "${RealWear.HF_COMMANDS}:${RealWear.normalizeVoiceCommand(keyword)}"

  override fun normalize(keywords: Array<String>): String =
    "${RealWear.HF_COMMANDS}:${keywords.joinToString("|") { RealWear.normalizeVoiceCommand(it) }}"

  override fun matches(keyword: String, command: String) = RealWear.matchesVoiceCommand(keyword, command)
}
