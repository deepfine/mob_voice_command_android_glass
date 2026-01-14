package com.deepfine.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deepfine.main.BottomSheetNavKey
import com.deepfine.main.DialogNavKey
import com.deepfine.main.SecondNavKey
import com.deepfine.navigator.LocalNavigator
import com.deepfine.presentation.theme.ApplicationTheme

@Composable
fun MainScreen() {
  ApplicationTheme {
    val navigator = LocalNavigator.current
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.Center),
        horizontalArrangement = Arrangement.SpaceEvenly,
      ) {
        SimpleButton(
          text = "SecondScreen",
          onClick = {
            navigator.navigate(SecondNavKey(123))
          },
        )

        SimpleButton(
          text = "Dialog",
          onClick = {
            navigator.navigate(DialogNavKey)
          },
        )

        SimpleButton(
          text = "BottomSheet",
          onClick = {
            navigator.navigate(BottomSheetNavKey)
          },
        )
      }
    }
  }
}

@Composable
private fun SimpleButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  text: String,
) {
  Box(
    modifier = modifier
      .border(width = 1.dp, color = Color.Black)
      .background(Color.White)
      .clickable(onClick = onClick)
      .padding(10.dp),
  ) {
    Text(
      text = text,
      color = Color.Black,
    )
  }
}
