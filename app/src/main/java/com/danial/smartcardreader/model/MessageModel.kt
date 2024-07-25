package com.danial.smartcardreader.model

import com.danial.smartcardreader.ui.utils.StringResource

sealed class MessageModel(val msg: StringResource) {
    class Message(message: StringResource) : MessageModel(msg = message)
    class ConnectionError(message: StringResource = StringResource.Text("Connection Error")) : MessageModel(msg = message)
    class ServerError(message: StringResource = StringResource.Text("Server Error")) : MessageModel(msg = message)
    class UnknownError(message: StringResource = StringResource.Text("Unknown Error")) : MessageModel(msg = message)
}
