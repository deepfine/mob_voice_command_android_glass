package com.deepfine.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deepfine.presentation.theme.ApplicationTheme

@Composable
fun SimpleDialog() {
  ApplicationTheme {
    Box(
      modifier = Modifier
        .size(200.dp)
        .background(Color.Gray),
    )
  }
}
