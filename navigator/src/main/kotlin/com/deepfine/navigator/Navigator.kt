package com.deepfine.navigator

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey

val LocalNavigator = compositionLocalOf<Navigator> {
  error("No Navigator found!")
}

interface Navigator {
  fun navigate(navKey: NavKey)
  fun navigateUp()

  companion object {
    fun from(backStack: SnapshotStateList<NavKey>): Navigator = NavigatorImpl(backStack)
  }
}

private class NavigatorImpl(
  private val backStack: SnapshotStateList<NavKey>,
) : Navigator {
  override fun navigate(navKey: NavKey) {
    backStack.add(navKey)
  }

  override fun navigateUp() {
    backStack.removeLastOrNull()
  }
}
