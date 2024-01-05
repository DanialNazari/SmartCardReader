package com.danial.smartcardreader.repository

import android.content.Context
import com.danial.smartcardreader.di.IoDispatcher
import com.danial.smartcardreader.network.ApiService
import com.danial.smartcardreader.ui.utils.ViewState
import com.danial.smartcardreader.ui.utils.isNetworkAvailable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType
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


	fun parsImage(file:File): Flow<ViewState<Any>> = flow {

		val request: MultipartBody.Part = MultipartBody.Part.createFormData(
			name = "file",
			filename = file.name,
			body = file.asRequestBody()
		)

		emit(ViewState.Loading)
		if (context.isNetworkAvailable()) {
			try {

				val response = api.parsImage(request)
				emit(ViewState.Success(response))

			} catch (e: Exception) {
				emit(checkResponseError(e))
			}
		} else {
			emit(ViewState.ConnectionError)
		}

	}.flowOn(ioDispatcher).catch { Timber.e(it) }

}