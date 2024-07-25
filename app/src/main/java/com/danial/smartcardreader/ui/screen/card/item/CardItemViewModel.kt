package com.danial.smartcardreader.ui.screen.card.item

import androidx.lifecycle.ViewModel
import com.danial.smartcardreader.model.CardItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CardItemViewModel @Inject constructor() :
    ViewModel() {


    private var _uiState = MutableStateFlow(CardDetailsUIState())
    val uiState: StateFlow<CardDetailsUIState>
        get() = _uiState.asStateFlow()

    fun setItem(cardItem: CardItemModel?) {
        _uiState.value = _uiState.value.copy(cardItem = cardItem)
    }

    data class CardDetailsUIState(
        val cardItem: CardItemModel? = null,
    )
}