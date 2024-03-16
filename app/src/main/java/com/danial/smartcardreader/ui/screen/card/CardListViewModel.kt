package com.danial.smartcardreader.ui.screen.card

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danial.smartcardreader.model.CardItemModel
import com.danial.smartcardreader.model.MessageModel
import com.danial.smartcardreader.model.TextRecognitionResponseModel
import com.danial.smartcardreader.repository.CardListRepository
import com.danial.smartcardreader.ui.utils.ViewState
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(private val cardListRepository: CardListRepository) :
    ViewModel() {

    private var _uiState = MutableStateFlow(CardListUIState())
    val uiState: StateFlow<CardListUIState>
        get() = _uiState.asStateFlow()

    init {
        getCardListFromCache()
    }

    private fun getCardListFromCache() {
        uiState.value.cardsList?.clear()
        Hawk.get<List<CardItemModel>>("cards_list")?.let {
            uiState.value.cardsList?.addAll(it)
        }

        _uiState.value = _uiState.value.copy(cardsList = uiState.value.cardsList)
    }

    fun addItem(item: CardItemModel) {
        uiState.value.cardsList?.add(item)
        Hawk.put("cards_list", uiState.value.cardsList)

        _uiState.value = _uiState.value.copy(cardsList = uiState.value.cardsList)
    }

    fun deleteItem(item: CardItemModel) {
        uiState.value.cardsList?.remove(item)
        Hawk.put("cards_list", uiState.value.cardsList)

        _uiState.value = _uiState.value.copy(cardsList = uiState.value.cardsList)
    }


    fun parseImage(file: File) {
        viewModelScope.launch {
            cardListRepository.parsImage(file).collect {
                when (it) {
                    is ViewState.Success -> {
                        _uiState.value = _uiState.value.copy(showLoading = true)

                        val textRecognitionResponseModel: TextRecognitionResponseModel =
                            it.data as TextRecognitionResponseModel
                        if (!textRecognitionResponseModel.ParsedResults.isNullOrEmpty()) {
                            textRecognitionResponseModel.ParsedResults[0]?.let { parsedResults ->
                                val lines = parsedResults.ParsedText?.replace(" ", "")?.split("\r\n")
                                var cardNumber: String? = null
                                var shebaNumber: String? = null
                                lines?.forEach { line ->

                                    if (line.isNotEmpty()) {
                                        if (line.length == 16) {
                                            cardNumber = line
                                        } else if (line.substring(0, 2) == "IR") {
                                            shebaNumber = line
                                        }
                                    }

                                }
                                if (cardNumber?.isNotEmpty() == true) {
                                    val cardItemModel = CardItemModel(number = cardNumber!!, sheba = shebaNumber)
                                    _uiState.value = _uiState.value.copy(addCardResult = cardItemModel)
                                } else {
                                    _uiState.value = _uiState.value.copy(message = MessageModel.ServerError("OCR api did get any valuable response"))
                                }
                            }
                        }

                    }

                    is ViewState.Loading -> {
                        _uiState.value = _uiState.value.copy(showLoading = true)
                    }

                    is ViewState.Error -> {
                        _uiState.value = _uiState.value.copy(showLoading = false, message = MessageModel.Message(it.message))
                    }

                    is ViewState.ConnectionError -> {
                        _uiState.value = _uiState.value.copy(showLoading = false, message = MessageModel.ConnectionError())
                    }

                    is ViewState.ServerError -> {
                        if (it.errors.isNullOrEmpty()) {
                            _uiState.value = _uiState.value.copy(showLoading = false, message = MessageModel.ServerError())
                        } else {
                            _uiState.value = _uiState.value.copy(showLoading = false, message = MessageModel.ServerError(it.errors[0]?.error ?: "Unknown Server Error"))
                        }
                    }

                    is ViewState.UnknownError -> {
                        _uiState.value = _uiState.value.copy(showLoading = false, message = MessageModel.UnknownError())
                    }
                }
            }
        }
    }

    data class CardListUIState(
        val showLoading: Boolean = false,
        val message: MessageModel? = null,
        val cardsList: ArrayList<CardItemModel>? = null,
        val addCardResult: CardItemModel? = null
    )
}