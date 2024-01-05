package com.danial.smartcardreader.repository

import com.danial.smartcardreader.model.ErrorResponseModel
import com.danial.smartcardreader.ui.utils.ViewState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.HttpException

open class BaseRepository{

    protected fun checkResponseError(responseException:Exception): ViewState<Any> {
        try {
            if(responseException is HttpException){
                val jsonString = responseException.response()?.errorBody()?.string()
                return convertStringToViewStateError(responseException.code(), jsonString)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return ViewState.UnknownError(message = responseException.message.toString())
    }

    private fun convertStringToViewStateError(code:Int, jsonString:String?):ViewState<Nothing>{
        val json =JSONTokener(jsonString).nextValue()
        return if (json is JSONObject) {
            val errorResponseModel = convertStringToError(jsonString)
            ViewState.ServerError(code, listOf(errorResponseModel))
        } else {
            val errors = convertStringToArrayError(jsonString)
            ViewState.ServerError(code,errors)
        }

    }

    private fun convertStringToError(jsonString:String?): ErrorResponseModel {
        return  Gson().fromJson(jsonString,ErrorResponseModel::class.java)
    }
    private fun convertStringToArrayError(jsonString:String?): List<ErrorResponseModel>{
        val typeToken = object : TypeToken<List<ErrorResponseModel>>() {}.type
        return Gson().fromJson(jsonString, typeToken)
    }

}