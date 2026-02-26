package com.deepfine.voicecommand.engine

interface VoiceCommandEngine {
  val action: String
  val extraKey: String

  fun normalize(keyword: String): String

  fun matches(keyword: String, command: String): Boolean

  /** phrase 등록 (Vuzix에서만 사용) */
  fun registerPhrases(phrases: List<String>) {}

  fun unregisterPhrases(phrases: List<String>) {}

  fun clearPhrases() {}
}
