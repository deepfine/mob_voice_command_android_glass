package com.deepfine.voicecommand.extensions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.clickable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.core.content.ContextCompat
import com.deepfine.voicecommand.utils.RealWear

/**
 * 리얼웨어 기기 화면에 기본적으로 표시되는 우측 하단 도움말 및
 * 클릭 가능한 UI 컴포넌트의 "항목 N 선택" 숫자 배지 숨김 처리
 */
fun Modifier.hideVoiceGuidance() = this@hideVoiceGuidance.semantics { contentDescription = RealWear.HF_HIDE_GUIDANCE }

/**
 * 특정 컴포넌트에 음성 명령 추가, 복수의 명령어 지정 가능
 * ex) "확대", "학대", "크게", ...
 * @param keywords 음성 명령어
 * @param onClick 음성 명령어 인식시 실행할 동작
 */
fun Modifier.voiceCommands(
  vararg keywords: String,
  onClick: () -> Unit,
): Modifier = this@voiceCommands
  .clearAndSetSemantics {
    contentDescription = "${RealWear.HF_COMMANDS}:${keywords.joinToString(",", transform = RealWear::normalizeVoiceCommand)}"
    onClick {
      onClick()
      true
    }
  }.clickable(onClick = onClick)

/**
 * 특정 컴포넌트에 지정한 개수만큼의 음성 명령 추가(항목 %d 선택, %d번 보기, 줌레벨 %d 등 1 ~ n까지의 명령어)
 * @param keyword 음성 명령어(%d 포함)
 * @param size 명령어 개수
 * @param onCommandReceive 음성 명령어 인식시 실행할 동작
 */
fun Modifier.voiceCommands(
  keyword: String,
  size: Int,
  onCommandReceive: (number: Int) -> Unit,
): Modifier = composed {
  val context = LocalContext.current
  val onClickState: (Int) -> Unit by rememberUpdatedState(onCommandReceive)

  val voiceCommandReceiver: BroadcastReceiver = remember {
    object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == RealWear.ACTION_SPEECH_EVENT) {
          val command = intent.getStringExtra(RealWear.EXTRA_SPEECH_COMMAND) ?: return
          if (RealWear.matchesVoiceCommand(keyword, command)) {
            command.firstNumber()?.let { number ->
              onClickState(number)
            }
          }
        }
      }
    }
  }

  DisposableEffect(context) {
    ContextCompat.registerReceiver(
      context,
      voiceCommandReceiver,
      IntentFilter(RealWear.ACTION_SPEECH_EVENT),
      ContextCompat.RECEIVER_EXPORTED,
    )

    onDispose {
      context.unregisterReceiver(voiceCommandReceiver)
    }
  }

  val resultCommand: String = buildString {
    append(RealWear.HF_ADD_COMMANDS)
    for (i in 1..size) {
      append(RealWear.normalizeVoiceCommand(String.format(keyword, i)))
      append("|")
    }
  }

  return@composed this@voiceCommands.clearAndSetSemantics {
    contentDescription = resultCommand
  }
}

private fun String.firstNumber(): Int? = Regex("\\d+").find(this)?.value?.toInt()
