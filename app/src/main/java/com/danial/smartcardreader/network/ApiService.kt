package com.danial.smartcardreader.network

import com.danial.smartcardreader.model.TextRecognitionResponseModel
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
	@Multipart
	@POST("/parse/image")
	suspend fun parsImage(
		@Part file: MultipartBody.Part
	): TextRecognitionResponseModel

}