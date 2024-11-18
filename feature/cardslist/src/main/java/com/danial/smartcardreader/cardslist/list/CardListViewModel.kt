package com.danial.smartcardreader.cardslist.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danial.network.model.CardItemModel
import com.danial.network.model.MessageModel
import com.danial.network.model.TextRecognitionResponseModel
import com.danial.smartcardreader.data.model.BaseResponse
import com.danial.smartcardreader.data.repository.CardListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(private val cardListRepository: CardListRepository) :
    ViewModel() {

    private var _uiState = MutableStateFlow(CardListUIState())
    val uiState: StateFlow<CardListUIState> = _uiState

    init {
        getCardListFromCache()
    }

    private fun getCardListFromCache() {
        _uiState.value.cardsList.clear()

        /*todo: should be read from repository*/
        /*Hawk.get<List<CardItemModel>>("cards_list")?.let {
            _uiState.value.cardsList.addAll(it)
        }*/

        _uiState.value = _uiState.value.copy(cardsList = _uiState.value.cardsList)
    }

    fun addItem(item: CardItemModel) {
        _uiState.value.cardsList.add(item)

        /*todo: should move to repository*/
        /*Hawk.put("cards_list", _uiState.value.cardsList)*/

        _uiState.value =
            _uiState.value.copy(cardsList = _uiState.value.cardsList, itemForAdd = null)
    }

    fun dismissAddCardItem() {
        _uiState.value = _uiState.value.copy(itemForAdd = null)
    }

    fun deleteItemConfirm(item: CardItemModel) {
        _uiState.value = _uiState.value.copy(itemForDelete = item)
    }

    fun deleteItem(item: CardItemModel) {
        _uiState.value.cardsList.remove(item)

        /*todo: should move to repository*/
        /*Hawk.put("cards_list", _uiState.value.cardsList)*/

        _uiState.value =
            _uiState.value.copy(cardsList = _uiState.value.cardsList, itemForDelete = null)
    }

    fun dismissDeleteItem() {
        _uiState.value = _uiState.value.copy(itemForDelete = null)
    }

    fun parseImage(file: File) {
        viewModelScope.launch {
            cardListRepository.parsImage(file).collect {
                when (it) {
                    is BaseResponse.Success -> {
                        handleParseImageSuccessState(it)
                    }

                    is BaseResponse.Loading -> {
                        _uiState.value = _uiState.value.copy(showLoading = true)
                    }

                    is BaseResponse.Error -> {
                        _uiState.value = _uiState.value.copy(
                            showLoading = false,
                            message = MessageModel.Message(/*todo: StringResource.Text(*/it.message)
                        )
                    }

                    is BaseResponse.ConnectionError -> {
                        _uiState.value = _uiState.value.copy(
                            showLoading = false,
                            message = MessageModel.ConnectionError()
                        )
                    }

                    is BaseResponse.ServerError -> {
                        if (it.errors.isNullOrEmpty()) {
                            _uiState.value = _uiState.value.copy(
                                showLoading = false,
                                message = MessageModel.ServerError()
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                showLoading = false,
                                message = MessageModel.ServerError(
                                    /* todo: StringResource.Text(it.errors[0]?.error ?:*/
                                    "Unknown Server Error"
                                )
                            )
                        }
                    }

                    is BaseResponse.UnknownError -> {
                        _uiState.value = _uiState.value.copy(
                            showLoading = false,
                            message = MessageModel.UnknownError()
                        )
                    }
                }
            }
        }
    }

    private fun handleParseImageSuccessState(it: BaseResponse.Success<TextRecognitionResponseModel>) {
        val (cardNumber, shebaNumber) = getCardData(it.data)
        if (cardNumber?.isNotEmpty() == true) {
            val cardItemModel = CardItemModel(number = cardNumber, sheba = shebaNumber)
            _uiState.value = _uiState.value.copy(
                itemForAdd = cardItemModel,
                showLoading = false
            )
        } else {
            val errorMessage = getErrorData(it.data)
            _uiState.value = _uiState.value.copy(
                message = MessageModel.ServerError(
                    /* todo: StringResource.Text(errorMessage ?: "OCR api did get any valuable response")*/
                    "OCR api did get any valuable response"
                ),
                showLoading = false
            )
        }
    }

    fun getCardItem(id: String?): CardItemModel? {
        return _uiState.value.cardsList.find { it.id == id }
    }

    private fun getCardData(textRecognitionResponseModel: TextRecognitionResponseModel?): Pair<String?, String?> {
        var cardNumber: String? = null
        var shebaNumber: String? = null

        textRecognitionResponseModel?.parsedResults?.get(0)?.let { parsedResults ->
            val lines = parsedResults.parsedText?.replace(" ", "")?.split("\r\n")

            lines?.forEach { line ->
                if (line.isNotEmpty()) {
                    if (line.length == 16) {
                        cardNumber = line
                    } else if (line.substring(0, 2) == "IR") {
                        shebaNumber = line
                    }
                }
            }
        }
        return Pair(cardNumber, shebaNumber)
    }

    private fun getErrorData(textRecognitionResponseModel: TextRecognitionResponseModel?): String? {
        var errorMessage: String? = null
        textRecognitionResponseModel?.errorMessage?.get(0)?.let { error ->
            errorMessage = error
        }
        return errorMessage
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    fun showImageSourceSelectionDialog() {
        _uiState.value = _uiState.value.copy(showImageSourceSelectionDialog = true)
    }

    fun dismissImageSourceSelectionDialog() {
        _uiState.value = _uiState.value.copy(showImageSourceSelectionDialog = false)
    }

    fun onCameraSelectAsImageSource() {
        _uiState.value = _uiState.value.copy(
            onCameraSelectedAsImageSource = true,
            showImageSourceSelectionDialog = false
        )
    }

    fun onGallerySelectAsImageSource() {
        _uiState.value = _uiState.value.copy(
            onGallerySelectedAsImageSource = true,
            showImageSourceSelectionDialog = false
        )
    }

    fun dismissOnSelectedAsImageSource() {
        _uiState.value = _uiState.value.copy(
            onCameraSelectedAsImageSource = false,
            onGallerySelectedAsImageSource = false,
        )
    }


    data class CardListUIState(
        val showLoading: Boolean = false,
        val message: MessageModel? = null,
        val cardsList: ArrayList<CardItemModel> = arrayListOf(),
        val addCardResult: CardItemModel? = null,
        val itemForAdd: CardItemModel? = null,
        val itemForDelete: CardItemModel? = null,
        val showImageSourceSelectionDialog: Boolean = false,

        val onCameraSelectedAsImageSource: Boolean = false,
        val onGallerySelectedAsImageSource: Boolean = false
    )

}