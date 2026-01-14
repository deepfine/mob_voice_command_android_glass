package com.deepfine.main.viewmodel

import androidx.lifecycle.ViewModel
import com.deepfine.main.model.SecondState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = SecondViewModel.Factory::class)
class SecondViewModel @AssistedInject constructor(
  @Assisted private val value: Int,
) : ViewModel() {

  private val _state = MutableStateFlow(SecondState(value))
  val state: StateFlow<SecondState>
    get() = _state.asStateFlow()

  @AssistedFactory
  interface Factory {
    fun create(value: Int): SecondViewModel
  }
}
