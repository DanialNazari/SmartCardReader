package com.danial.network.model

sealed class MessageModel(val msg: String) {
    class Message(message: String) : MessageModel(msg = message)
    class ConnectionError(message: String = "Connection Error") : MessageModel(msg = message)
    class ServerError(message: String = "Server Error") : MessageModel(msg = message)
    class UnknownError(message: String = "Unknown Error") : MessageModel(msg = message)
}
