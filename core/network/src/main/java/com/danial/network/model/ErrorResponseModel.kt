package com.danial.network.model

data class ErrorResponseModel(
    val description: Any,
    val error: String,
    val errorCode: Int
)