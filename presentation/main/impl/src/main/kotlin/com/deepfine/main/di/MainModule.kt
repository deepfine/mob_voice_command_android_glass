package com.deepfine.main.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.DialogSceneStrategy
import com.deepfine.main.BottomSheetNavKey
import com.deepfine.main.DialogNavKey
import com.deepfine.main.MainNavKey
import com.deepfine.main.SecondNavKey
import com.deepfine.main.ui.MainScreen
import com.deepfine.main.ui.SecondScreen
import com.deepfine.main.ui.SimpleBottomSheet
import com.deepfine.main.ui.SimpleDialog
import com.deepfine.main.viewmodel.SecondViewModel
import com.deepfine.presentation.strategy.BottomSheetSceneStrategy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object MainModule {
  @IntoSet
  @Provides
  fun provideMainEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit = {
    entry<MainNavKey> {
      MainScreen()
    }

    entry<SecondNavKey> { key: SecondNavKey ->
      SecondScreen(
        viewModel = hiltViewModel<SecondViewModel, SecondViewModel.Factory> { factory ->
          factory.create(key.value)
        },
      )
    }

    entry<DialogNavKey>(
      metadata = DialogSceneStrategy.dialog(),
    ) {
      SimpleDialog()
    }

    entry<BottomSheetNavKey>(
      metadata = BottomSheetSceneStrategy.bottomSheet(),
    ) {
      SimpleBottomSheet()
    }
  }
}
