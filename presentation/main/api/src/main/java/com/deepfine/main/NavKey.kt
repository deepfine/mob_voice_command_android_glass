package com.deepfine.main

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object MainNavKey : NavKey

@Serializable
data class SecondNavKey(val value: Int) : NavKey

@Serializable
object DialogNavKey : NavKey

@Serializable
object BottomSheetNavKey : NavKey
