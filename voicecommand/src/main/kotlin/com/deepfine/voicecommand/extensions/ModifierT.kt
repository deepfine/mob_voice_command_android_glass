package com.deepfine.voicecommand.extensions

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.core.content.ContextCompat
import com.deepfine.voicecommand.engine.LocalVoiceEngine
import com.deepfine.voicecommand.engine.RealWearEngine
import com.deepfine.voicecommand.engine.VuzixEngine
import com.deepfine.voicecommand.utils.RealWear

fun Modifier.hideVoiceGuidance() = this@hideVoiceGuidance.semantics { contentDescription = RealWear.HF_HIDE_GUIDANCE }

@SuppressLint("ModifierFactoryUnreferencedReceiver", "RememberReturnType")
@Composable
fun Modifier.voiceCommands(
  vararg keywords: String,
  onClick: () -> Unit,
): Modifier {
  val engine = LocalVoiceEngine.current
  val context = LocalContext.current
  val onClickState by rememberUpdatedState(onClick)

  DisposableEffect(engine, keywords) {
    engine.registerPhrases(
      keywords.map { engine.normalize(it) },
    )

    onDispose {
      engine.unregisterPhrases(keywords.map { engine.normalize(it) })
    }
  }
//  LaunchedEffect(engine, keywords) {
//    engine.registerPhrases(
//      keywords.map { engine.normalize(it) },
//    )
//  }

  val receiver = remember(engine) {
    object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        Log.d("HSR", "received: ${intent.action}")
        if (intent.action == engine.action) {
          val command = intent.getStringExtra(engine.extraKey) ?: return
          Log.d("HSR", "command: $command")

          if (keywords.any { engine.matches(it, command) }) {
            onClickState()
          }
        }
      }
    }
  }

  DisposableEffect(context, engine) {
    ContextCompat.registerReceiver(
      context,
      receiver,
      IntentFilter(engine.action),
      ContextCompat.RECEIVER_EXPORTED,
    )

    onDispose {
      context.unregisterReceiver(receiver)
    }
  }

  return when (engine) {
    is RealWearEngine -> {
      this.clearAndSetSemantics {
        contentDescription = "${RealWear.HF_COMMANDS}:${
          keywords.joinToString(",") {
            engine.normalize(it)
          }
        }"
        onClick {
          onClick()
          true
        }
      }
    }

    is VuzixEngine -> {
      this.clickable { onClick() }
    }

    else -> {
      throw RuntimeException()
    }
  }
}

@Composable
fun Modifier.voiceCommands(
  keyword: String,
  index: Int,
  size: Int,
  onCommandReceive: (Int) -> Unit,
): Modifier {
  val engine = LocalVoiceEngine.current
  val context = LocalContext.current
  val onClickState by rememberUpdatedState(onCommandReceive)

  val expandedKeywords = remember(keyword, size) {
    (1..size).map { index ->
      String.format(keyword, index)
    }
  }

  LaunchedEffect(engine, expandedKeywords) {
    engine.registerPhrases(
      expandedKeywords.map { engine.normalize(it) },
    )
  }

  val receiver = remember(engine) {
    object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == engine.action) {
          val command = intent.getStringExtra(engine.extraKey) ?: return

          if (expandedKeywords.any { engine.matches(it, command) }) {
            command.firstNumber()?.let {
              onClickState(it)
            }
          }
//          if (engine.matches(keyword, command)) {
//            command.firstNumber()?.let {
//              onClickState(it)
//            }
//          }
        }
      }
    }
  }

  DisposableEffect(context, engine) {
    ContextCompat.registerReceiver(
      context,
      receiver,
      IntentFilter(engine.action),
      ContextCompat.RECEIVER_EXPORTED,
    )

    onDispose {
      context.unregisterReceiver(receiver)
    }
  }

  return when (engine) {
    is RealWearEngine -> {
      val resultCommand = buildString {
        append(RealWear.HF_ADD_COMMANDS)
        expandedKeywords.forEach {
          append(engine.normalize(it))
          append("|")
        }
      }

      this.clearAndSetSemantics {
        contentDescription = resultCommand
      }
    }

    is VuzixEngine -> {
      this.clickable {
        onClickState(index)
      }
    }

    else -> {
      throw RuntimeException()
    }
  }
}

private fun String.firstNumber(): Int? = Regex("\\d+").find(this)?.value?.toInt()
