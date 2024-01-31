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
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(private val cardListRepository: CardListRepository) :
    ViewModel() {

    var isLoading by mutableStateOf(false)
    val messageStateFlow = mutableStateOf<MessageModel?>(null)

    val cardsList = mutableListOf<CardItemModel>()
    val addCardResult = mutableStateOf<CardItemModel?>(null)

    init {
        getCardListFromCache()
    }

    private fun getCardListFromCache() {
        cardsList.clear()
        Hawk.get<List<CardItemModel>>("cards_list")?.let {
            cardsList.addAll(it)
        }
    }

    fun addItem(item: CardItemModel) {
        cardsList.add(item)
        Hawk.put("cards_list", cardsList)
    }
    fun deleteItem(item: CardItemModel) {
        cardsList.remove(item)
        Hawk.put("cards_list", cardsList)
    }


    fun parseImage(file: File) {
        viewModelScope.launch {
            cardListRepository.parsImage(file).collect {
                when (it) {
                    is ViewState.Success -> {
                        isLoading = false

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
                                    addCardResult.value = cardItemModel
                                } else {
                                    messageStateFlow.value = MessageModel("OCR api did get any valuable response")
                                }
                            }
                        }

                    }

                    is ViewState.Loading -> {
                        isLoading = true
                    }

                    is ViewState.Error -> {
                        isLoading = false
                        messageStateFlow.value = MessageModel(it.message)
                    }

                    is ViewState.ConnectionError -> {
                        isLoading = false
                        messageStateFlow.value = MessageModel("Connection Error")


                    }

                    is ViewState.ServerError -> {
                        isLoading = false
                        if (it.errors.isNullOrEmpty()) {
                            messageStateFlow.value = MessageModel("Unknown Server Error")
                        } else {
                            messageStateFlow.value = MessageModel(
                                it.errors[0]?.error ?: "Unknown Server Error"
                            )
                        }
                    }

                    is ViewState.UnknownError -> {
                        isLoading = false
                        messageStateFlow.value = MessageModel("Unknown Error")
                    }
                }
            }
        }
    }
}