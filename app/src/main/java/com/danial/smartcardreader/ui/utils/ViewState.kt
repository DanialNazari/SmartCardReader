package com.danial.smartcardreader.ui.utils

import com.danial.smartcardreader.model.ErrorResponseModel

sealed class ViewState<out R> {
    object Loading : ViewState<Nothing>()
    data class Success<out T>(val data: T?) : ViewState<T>()
    data class Error(val message:String): ViewState<Nothing>()
    data class ServerError(val statusCode:Int,val errors: List<ErrorResponseModel?>?) : ViewState<Nothing>()
    data class UnknownError(val message:String): ViewState<Nothing>()
    object ConnectionError : ViewState<Nothing>()
}