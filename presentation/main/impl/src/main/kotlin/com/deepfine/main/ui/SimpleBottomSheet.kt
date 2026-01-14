package com.deepfine.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.deepfine.presentation.theme.ApplicationTheme

@Composable
fun SimpleBottomSheet() {
  ApplicationTheme {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.Gray),
    )
  }
}
