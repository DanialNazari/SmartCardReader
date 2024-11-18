package com.danial.smartcardreader.data.model

import com.danial.network.model.ErrorResponseModel

sealed class BaseResponse<out R> {
    data object Loading : BaseResponse<Nothing>()
    data class Success<out T>(val data: T?) : BaseResponse<T>()
    data class Error(val message:String): BaseResponse<Nothing>()
    data class ServerError(val statusCode:Int,val errors: List<ErrorResponseModel?>?) : BaseResponse<Nothing>()
    data class UnknownError(val message:String): BaseResponse<Nothing>()
    data object ConnectionError : BaseResponse<Nothing>()
}