package com.danial.smartcardreader.model

sealed class MessageModel {
    class Message(val message: String) : MessageModel()
    class ConnectionError(val message: String = "Connection Error") : MessageModel()
    class ServerError(val message: String = "Server Error") : MessageModel()
    class UnknownError(val message: String = "Unknown Error") : MessageModel()
}
