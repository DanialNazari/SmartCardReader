package com.danial.smartcardreader.model

import com.danial.smartcardreader.ui.utils.StringResource

sealed class MessageModel {
    class Message(val message: StringResource) : MessageModel()
    class ConnectionError(val message: StringResource = StringResource.Text("Connection Error")) : MessageModel()
    class ServerError(val message: StringResource = StringResource.Text("Server Error")) : MessageModel()
    class UnknownError(val message: StringResource = StringResource.Text("Unknown Error")) : MessageModel()
}
