package com.danial.smartcardreader.data.repository

import android.content.Context
import com.danial.network.model.TextRecognitionResponseModel
import com.danial.network.retrofit.ApiService
import com.danial.smartcardreader.data.di.IoDispatcher
import com.danial.smartcardreader.data.model.BaseResponse
import com.danial.smartcardreader.data.util.isNetworkAvailable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardListRepository @Inject constructor(
    private val api: ApiService,
    private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseRepository() {


    fun parsImage(file: File): Flow<BaseResponse<TextRecognitionResponseModel>> = flow {
        emit(BaseResponse.Loading)
        val request: MultipartBody.Part = MultipartBody.Part.createFormData(
            name = "file",
            filename = file.name,
            body = file.asRequestBody()
        )
        if (context.isNetworkAvailable()) {
            try {
                val response = api.parsImage(request)
                emit(BaseResponse.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(checkResponseError(e))
            }
        } else {
            emit(BaseResponse.ConnectionError)
        }

        // mock data
        /*emit(ViewState.Success(data = mockData))*/

    }.flowOn(ioDispatcher).catch { Timber.e(it) }


    private val mockData = TextRecognitionResponseModel(
        parsedResults = arrayListOf(
            TextRecognitionResponseModel.ParsedResult(parsedText = "5022291307412589"),
            TextRecognitionResponseModel.ParsedResult(parsedText = "IR502229130741258914523698")
        )
    )

}