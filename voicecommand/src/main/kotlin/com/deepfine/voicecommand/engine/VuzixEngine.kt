package com.deepfine.voicecommand.engine

import android.util.Log
import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient

class VuzixEngine(private val speechClient: VuzixSpeechClient) : VoiceCommandEngine {
  override val action = VuzixSpeechClient.ACTION_VOICE_COMMAND
  override val extraKey = VuzixSpeechClient.PHRASE_STRING_EXTRA

  override fun normalize(keyword: String) = keyword

  override fun matches(keyword: String, command: String): Boolean {
    Log.d("HSR", "keyword: $keyword, command: $command")
    return keyword.replace(" ", "") == command
  }

  override fun registerPhrases(phrases: List<String>) {
    phrases.forEach { phrase ->
      val substitution = phrase.replace(" ", "")
      Log.d("HSR", phrase)
      speechClient.insertPhrase(phrase, substitution)
    }
  }

  override fun unregisterPhrases(phrases: List<String>) {
    phrases.forEach {
      speechClient.deletePhrase(it)
    }
  }

  override fun clearPhrases() {
    speechClient.deleteAllPhrases()
  }
}
