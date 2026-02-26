package com.deepfine.voicecommand.demo

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.provider.Settings.ACTION_SETTINGS
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.deepfine.voicecommand.engine.LocalVoiceEngine
import com.deepfine.voicecommand.engine.RealWearEngine
import com.deepfine.voicecommand.engine.VuzixEngine
import com.deepfine.voicecommand.extensions.hideVoiceGuidance
import com.deepfine.voicecommand.extensions.voiceCommands
import com.deepfine.voicecommand.utils.isVuzixDevice
import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    installSplashScreen()

    setContent {
      val engine = remember {
        if (isVuzixDevice()) {
          VuzixEngine(speechClient = VuzixSpeechClient(this))
        } else {
          RealWearEngine
        }
      }

      CompositionLocalProvider(LocalVoiceEngine provides engine) {
        MainScreen()
      }
    }
  }
}

@Composable
private fun MainScreen() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .hideVoiceGuidance()
      .padding(vertical = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    val context: Context = LocalContext.current
    val resources: Resources = LocalResources.current

    val textStyle = TextStyle(
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold,
    )

    Text(
      text = stringResource(R.string.main_guide1),
      style = textStyle,
    )

    Box(
      modifier = Modifier
        .background(color = Color.LightGray)
        .voiceCommands(
          stringResource(R.string.main_option),
          onClick = {
            showToast(context, resources.getString(R.string.main_settings_click_message))
            context.startActivity(Intent(ACTION_SETTINGS))
          },
        ).padding(
          horizontal = 12.dp,
          vertical = 8.dp,
        ),
    ) {
      Text(
        text = stringResource(R.string.main_option),
        style = textStyle,
      )
    }

    Text(
      text = stringResource(R.string.main_guide2),
      style = textStyle,
    )

    val itemSize = 10
    val itemState = rememberLazyListState()

    var selectedItemNumber: Int? by remember { mutableStateOf(null) }

    LazyColumn(
      modifier = Modifier
        .background(color = Color.LightGray)
        .size(
          width = 320.dp,
          height = 160.dp,
        ).voiceCommands(
          keyword = stringResource(R.string.main_item_keyword),
          size = itemSize,
          index = selectedItemNumber ?: 0,
          onCommandReceive = { number ->
            selectedItemNumber = number
            showToast(context, resources.getString(R.string.main_item_click_message, number))
          },
        ),
      state = itemState,
    ) {
      items(itemSize) { index ->
        Text(
          text = stringResource(R.string.main_item, index + 1),
          style = TextStyle(
            fontSize = 16.sp,
            color = if (index + 1 == selectedItemNumber) Color.Red else Color.Black,
          ),
        )

        if (index + 1 < itemSize) {
          Spacer(modifier = Modifier.height(2.dp))
        }
      }
    }

    Row(
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      val scope = rememberCoroutineScope()

      Text(
        modifier = Modifier.voiceCommands(
          stringResource(R.string.main_page_up),
          onClick = {
            scope.launch {
              itemState.animateScrollToItem(0)
            }
          },
        ),
        text = "“${stringResource(R.string.main_page_up)}”",
        style = textStyle,
      )
      Text(
        modifier = Modifier.voiceCommands(
          stringResource(R.string.main_page_down),
          onClick = {
            scope.launch {
              itemState.animateScrollToItem(itemSize - 1)
            }
          },
        ),
        text = "“${stringResource(R.string.main_page_down)}”",
        style = textStyle,
      )
    }
  }
}

private fun showToast(context: Context, message: String) {
  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview(widthDp = 640, heightDp = 360, showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun MainActivityPreview() {
  MainScreen()
}
