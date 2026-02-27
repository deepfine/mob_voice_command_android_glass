package com.deepfine.voicecommand.extensions

import android.content.IntentFilter
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.core.content.ContextCompat
import com.deepfine.voicecommand.engine.RealWearEngine
import com.deepfine.voicecommand.engine.VoiceCommandEngine
import com.deepfine.voicecommand.engine.VuzixEngine
import com.deepfine.voicecommand.engine.rememberVoiceCommandEngine
import com.deepfine.voicecommand.model.DeviceType
import com.deepfine.voicecommand.utils.DeviceTypeUtil
import com.deepfine.voicecommand.utils.RealWear
import kotlinx.coroutines.launch

/**
 * 리얼웨어 기기 화면에 기본적으로 표시되는 우측 하단 도움말 및
 * 클릭 가능한 UI 컴포넌트의 "항목 N 선택" 숫자 배지 숨김 처리
 */
@Composable
private fun Modifier.hideVoiceGuidance() = this@hideVoiceGuidance.semantics { contentDescription = RealWear.HF_HIDE_GUIDANCE }

@Composable
fun Modifier.voiceCommand(
  keyword: String,
  onClick: () -> Unit,
  enabled: Boolean = true,
): Modifier {
  val context = LocalContext.current
  val activity = LocalActivity.current

  val engine = rememberVoiceCommandEngine(activity)
  val onClickState by rememberUpdatedState(onClick)

  if (engine is VuzixEngine) {
    DisposableEffect(context, engine) {
      val receiver = object : VoiceCommandEngine.VoiceCommandReceiver(engine) {
        override fun onCommandReceive(command: String) {
          if (engine.matches(keyword, command)) {
            onClickState()
          }
        }
      }

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
  }

  return when (engine) {
    is RealWearEngine, is VuzixEngine -> {
      this.clearAndSetSemantics {
        if (enabled) {
          contentDescription = engine.normalize(keyword)
          onClick {
            onClick()
            true
          }
        }
      }
    }

    else -> {
      throw RuntimeException()
    }
  }
}

/**
 * 특정 컴포넌트에 음성 명령 추가, 복수의 명령어 지정 가능
 * ex) "확대", "학대", "크게", ...
 * @param keywords 음성 명령어
 * @param onClick 음성 명령어 인식시 실행할 동작
 */
@Composable
fun Modifier.voiceCommands(
  vararg keywords: String,
  onClick: () -> Unit,
  enabled: Boolean = true,
): Modifier {
  val context = LocalContext.current
  val activity = LocalActivity.current

  val engine = rememberVoiceCommandEngine(activity)

  val onClickState by rememberUpdatedState(onClick)

  if (engine is VuzixEngine) {
    DisposableEffect(context, engine) {
      val receiver = object : VoiceCommandEngine.VoiceCommandReceiver(engine) {
        override fun onCommandReceive(command: String) {
          if (keywords.any { engine.matches(it, command) }) {
            onClickState()
          }
        }
      }

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
  }

  return when (engine) {
    is RealWearEngine, is VuzixEngine -> {
      this.clearAndSetSemantics {
        if (enabled) {
          contentDescription = engine.normalize(keywords.toList().toTypedArray(), separator = ",")
          onClick {
            onClick()
            true
          }
        }
      }
    }

    else -> {
      this.clickable(enabled = enabled, onClick = onClick)
    }
  }
}

/**
 * 특정 컴포넌트에 지정한 개수만큼의 음성 명령 추가(항목 %d 선택, %d번 보기, 줌레벨 %d 등 1 ~ n까지의 명령어)
 * @param keyword 음성 명령어(%d 포함)
 * @param size 명령어 개수
 * @param onCommandReceive 음성 명령어 인식시 실행할 동작
 */
@Composable
fun Modifier.voiceCommands(
  keyword: String,
  size: Int,
  onCommandReceive: (Int) -> Unit,
  enabled: Boolean = true,
): Modifier {
  val context = LocalContext.current
  val activity = LocalActivity.current

  val engine = rememberVoiceCommandEngine(activity)

  val onClickState by rememberUpdatedState(onCommandReceive)

  val expandedKeywords = remember(keyword, size) {
    (1..size).map { index ->
      String.format(keyword, index)
    }
  }

  DisposableEffect(context, engine) {
    val receiver = object : VoiceCommandEngine.VoiceCommandReceiver(engine) {
      override fun onCommandReceive(command: String) {
        if (expandedKeywords.any { engine!!.matches(it, command) }) {
          command.firstNumber()?.let {
            onClickState(it)
          }
        }
      }
    }

    ContextCompat.registerReceiver(
      context,
      receiver,
      IntentFilter(engine?.action),
      ContextCompat.RECEIVER_EXPORTED,
    )

    onDispose {
      context.unregisterReceiver(receiver)
    }
  }

  return when (engine) {
    is RealWearEngine, is VuzixEngine -> {
      if (enabled) {
        this.clearAndSetSemantics {
          contentDescription = engine.normalize(expandedKeywords.toTypedArray())
        }
      } else {
        this
      }
    }

    else -> {
      this
    }
  }
}

@Composable
fun Modifier.commandRoot(): Modifier = when (DeviceTypeUtil.getDeviceType()) {
  DeviceType.VUZIX -> iterateVoiceCommands()
  DeviceType.REALWEAR -> hideVoiceGuidance()
  DeviceType.UNKNOWN -> this
}

@Composable
private fun Modifier.iterateVoiceCommands() = this
  .composed {
    val view = LocalView.current
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()

    val engine = rememberVoiceCommandEngine(activity)

    fun iterateContentDescriptions() {
      scope.launch {
        withFrameNanos { }
        val keywords = getSemanticsNodesFromView(view)?.mapNotNull {
          it.config.getOrNull(SemanticsProperties.ContentDescription)?.firstOrNull()
        } ?: return@launch

        val processedKeywords = mutableListOf<String>()
        keywords.forEach { keyword ->
          if (keyword.contains("|")) {
            processedKeywords.addAll(keyword.split("|"))
          } else {
            processedKeywords.add(keyword)
          }
        }

        if (engine is VuzixEngine && processedKeywords.isNotEmpty()) {
          Log.d(TAG, processedKeywords.joinToString("|") { engine.normalize(it) })
          engine.clearPhrases()
          engine.registerPhrases(
            processedKeywords
              .map { engine.normalize(it) }
              .filter(String::isNotEmpty),
          )
        }
      }
    }

    SideEffect {
      iterateContentDescriptions()
    }

    DisposableEffect(view) {
      var focusLost = false
      val listener = ViewTreeObserver.OnWindowFocusChangeListener { hasFocus ->
        if (hasFocus && focusLost) {
          iterateContentDescriptions()
        } else if (!hasFocus) {
          focusLost = true
        }
      }

      view.viewTreeObserver.addOnWindowFocusChangeListener(listener)

      onDispose {
        view.viewTreeObserver.removeOnWindowFocusChangeListener(listener)
      }
    }

    this
  }

private fun getSemanticsNodesFromView(view: View): List<SemanticsNode>? {
  return try {
    val clazz = Class.forName("androidx.compose.ui.platform.AndroidComposeView")
    if (!clazz.isInstance(view)) return null

    val field = clazz.getDeclaredField("semanticsOwner")
    field.isAccessible = true
    val owner = field.get(view)

    val ktClass = Class.forName(
      "androidx.compose.ui.semantics.SemanticsOwnerKt",
    )

    val method = ktClass.getDeclaredMethod(
      "getAllSemanticsNodes",
      owner.javaClass,
      Boolean::class.javaPrimitiveType,
      Boolean::class.javaPrimitiveType,
    )
    method.isAccessible = true

    method.invoke(null, owner, true, true) as? List<SemanticsNode>
  } catch (e: Throwable) {
    e.printStackTrace()
    null
  }
}

private const val TAG = "speech"

private fun String.firstNumber(): Int? = Regex("\\d+").find(this)?.value?.toInt()
