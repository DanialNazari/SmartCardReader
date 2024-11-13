package com.danial.smartcardreader.network.model

import com.google.gson.annotations.SerializedName

data class TextRecognitionResponseModel(
    @SerializedName("IsErroredOnProcessing") val isErroredOnProcessing: Boolean? = null,
    @SerializedName("OCRExitCode") val ocrExitCode: Int? = null,
    @SerializedName("ParsedResults") val parsedResults: List<ParsedResult?>? = null,
    @SerializedName("SearchablePDFURL") val processingTimeInMilliseconds: String? = null,
    @SerializedName("SearchablePDFURL") val searchablePDFURL: String? = null,
    @SerializedName("ErrorMessage") val errorMessage: List<String>? = null
) {
    data class ParsedResult(
        @SerializedName("ErrorDetails") val errorDetails: String? = null,
        @SerializedName("ErrorMessage") val errorMessage: String? = null,
        @SerializedName("FileParseExitCode") val fileParseExitCode: Int? = null,
        @SerializedName("ParsedText") val parsedText: String? = null,
        @SerializedName("TextOrientation") val textOrientation: String? = null,
        @SerializedName("TextOverlay") val textOverlay: TextOverlayModel? = null
    ) {
        data class TextOverlayModel(
            @SerializedName("HasOverlay") val hasOverlay: Boolean? = null,
            @SerializedName("Lines") val lines: List<Any>? = null,
            @SerializedName("Message") val message: String? = null
        )
    }
}