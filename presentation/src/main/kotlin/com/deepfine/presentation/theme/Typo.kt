package com.deepfine.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.deepfine.presentation.R

private val notosanskr = FontFamily(
  Font(R.font.noto_sans_kr_100, FontWeight.W100),
  Font(R.font.noto_sans_kr_200, FontWeight.W200),
  Font(R.font.noto_sans_kr_300, FontWeight.W300),
  Font(R.font.noto_sans_kr_400, FontWeight.W400),
  Font(R.font.noto_sans_kr_500, FontWeight.W500),
  Font(R.font.noto_sans_kr_600, FontWeight.W600),
  Font(R.font.noto_sans_kr_700, FontWeight.W700),
  Font(R.font.noto_sans_kr_800, FontWeight.W800),
  Font(R.font.noto_sans_kr_900, FontWeight.W900),
)

val defaultTextStyle = TextStyle(
  fontFamily = notosanskr,
  platformStyle = PlatformTextStyle(includeFontPadding = false),
  color = Color.Black,
)

// Set of Material typography styles to start with
val Typography = Typography(
  bodyLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp,
  ),
  /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
   */
)
