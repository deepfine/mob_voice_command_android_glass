package com.deepfine.scheme.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.deepfine.main.MainNavKey
import com.deepfine.navigator.LocalNavigator
import com.deepfine.navigator.Navigator
import com.deepfine.presentation.strategy.BottomSheetSceneStrategy
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SchemeActivity : ComponentActivity() {
  @Inject
  lateinit var entryBuilders: Set<@JvmSuppressWildcards EntryProviderScope<NavKey>.() -> Unit>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    installSplashScreen()
    enableEdgeToEdge()

    setContent {
      val backStack = remember { mutableStateListOf<NavKey>(MainNavKey) }

      val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }
      val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }
      val singlePaneStrategy = remember { SinglePaneSceneStrategy<NavKey>() }

      CompositionLocalProvider(
        LocalNavigator provides Navigator.from(backStack),
      ) {
        NavDisplay(
          backStack = backStack,
          onBack = { backStack.removeLastOrNull() },
          sceneStrategy = bottomSheetStrategy then dialogStrategy then singlePaneStrategy,
          entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
          ),
          entryProvider = entryProvider {
            entryBuilders.forEach { builder -> this.builder() }
          },
          transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
              slideOutHorizontally(targetOffsetX = { -it })
          },
          popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
              slideOutHorizontally(targetOffsetX = { it })
          },
          predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
              slideOutHorizontally(targetOffsetX = { it })
          },
        )
      }
    }
  }
}
