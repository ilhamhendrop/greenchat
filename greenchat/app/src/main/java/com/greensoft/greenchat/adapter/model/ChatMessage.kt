package com.greensoft.greenchat.adapter.model

data class ChatMessage (
        var id: String?,
        var chat: String?,
        var fromId: String?,
        var toId: String?,
        var timeStamp: Long?) {
    constructor() : this("", "", "", "", -1)
}