package com.danial.smartcardreader.model

data class TextRecognitionResponseModel(
    val IsErroredOnProcessing: Boolean? = null,
    val OCRExitCode: Int? = null,
    val ParsedResults: List<ParsedResult?>? = null,
    val ProcessingTimeInMilliseconds: String? = null,
    val SearchablePDFURL: String? = null
) {
    data class ParsedResult(
        val ErrorDetails: String? = null,
        val ErrorMessage: String? = null,
        val FileParseExitCode: Int? = null,
        val ParsedText: String? = null,
        val TextOrientation: String? = null,
        val TextOverlay: TextOverlayModel? = null
    ) {
        data class TextOverlayModel(
            val HasOverlay: Boolean? = null,
            val Lines: List<Any>? = null,
            val Message: String? = null
        )
    }
}