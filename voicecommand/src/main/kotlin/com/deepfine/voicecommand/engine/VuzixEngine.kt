package com.deepfine.voicecommand.engine

import android.app.Activity
import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient

class VuzixEngine(activity: Activity?) : VoiceCommandEngine {
  private val speechClient = VuzixSpeechClient(activity)

  override val action = VuzixSpeechClient.ACTION_VOICE_COMMAND
  override val extraKey = VuzixSpeechClient.PHRASE_STRING_EXTRA

  init {
    if (!VuzixSpeechClient.isRecognizerEnabled(activity)) {
      VuzixSpeechClient.EnableRecognizer(activity, true)
    }

    if (!VuzixSpeechClient.isRecognizerTriggered(activity)) {
      VuzixSpeechClient.TriggerVoiceAudio(activity, true)
    }
  }

  override fun normalize(keyword: String) = keyword

  override fun matches(keyword: String, command: String): Boolean = keyword.replace(" ", "") == command

  override fun registerPhrases(phrases: List<String>) {
    phrases.forEach { phrase ->
      val substitution = phrase.replace(" ", "")
      speechClient.insertPhrase(phrase, substitution)
    }
  }

  override fun clearPhrases() {
    speechClient.deleteAllPhrases()
  }
}
