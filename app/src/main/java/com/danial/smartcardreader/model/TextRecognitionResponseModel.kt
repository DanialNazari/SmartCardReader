package com.danial.smartcardreader.model

data class TextRecognitionResponseModel(
    val IsErroredOnProcessing: Boolean,
    val OCRExitCode: Int,
    val ParsedResults: List<ParsedResult?>?,
    val ProcessingTimeInMilliseconds: String,
    val SearchablePDFURL: String
) {
    data class ParsedResult(
        val ErrorDetails: String,
        val ErrorMessage: String,
        val FileParseExitCode: Int,
        val ParsedText: String?,
        val TextOrientation: String,
        val TextOverlay: TextOverlayModel
    ) {
        data class TextOverlayModel(
            val HasOverlay: Boolean,
            val Lines: List<Any>,
            val Message: String
        )
    }
}