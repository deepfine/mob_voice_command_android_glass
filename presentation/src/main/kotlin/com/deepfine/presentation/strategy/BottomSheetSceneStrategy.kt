package com.deepfine.presentation.strategy

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

private class BottomSheetScene<T : Any>(
  override val key: Any,
  override val previousEntries: List<NavEntry<T>>,
  override val overlaidEntries: List<NavEntry<T>>,
  private val entry: NavEntry<T>,
  private val modalBottomSheetProperties: ModalBottomSheetProperties,
  private val onBack: () -> Unit,
) : OverlayScene<T> {
  override val entries: List<NavEntry<T>> = listOf(entry)

  override val content: @Composable (() -> Unit) = {
    ModalBottomSheet(
      onDismissRequest = onBack,
      properties = modalBottomSheetProperties,
    ) {
      entry.Content()
    }
  }
}

class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {
  override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
    val lastEntry = entries.lastOrNull()
    val modalBottomSheetProperties = lastEntry?.metadata?.get(BOTTOM_SHEET_KEY) as? ModalBottomSheetProperties
    return modalBottomSheetProperties?.let { properties ->
      BottomSheetScene(
        key = lastEntry.contentKey,
        previousEntries = entries.dropLast(1),
        overlaidEntries = entries.dropLast(1),
        entry = lastEntry,
        modalBottomSheetProperties = properties,
        onBack = onBack,
      )
    }
  }

  companion object {
    fun bottomSheet(
      modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
    ): Map<String, Any> = mapOf(BOTTOM_SHEET_KEY to modalBottomSheetProperties)

    internal const val BOTTOM_SHEET_KEY = "bottomSheet"
  }
}
