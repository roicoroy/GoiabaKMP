package com.goiaba.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class DetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var id:String = savedStateHandle.get<String>("id") ?: ""

}