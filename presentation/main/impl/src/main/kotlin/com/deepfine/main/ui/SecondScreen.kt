package com.deepfine.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.deepfine.main.viewmodel.SecondViewModel
import com.deepfine.presentation.theme.ApplicationTheme

@Composable
fun SecondScreen(
  viewModel: SecondViewModel,
) {
  val state by viewModel.state.collectAsState()

  ApplicationTheme {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Red),
    ) {
      Text(
        modifier = Modifier.align(Alignment.Center),
        text = state.value.toString(),
        color = Color.White,
      )
    }
  }
}
