package com.danial.network.model

data class TextRecognitionModel(
    var message: Message? = null,
    val organizationId: Int?,
    var receiverUserId: Int? = null,
    var subject: String ? = null
) {
    data class Message(
        val attachments: String? = null,
        var body: String? = null
    )
}