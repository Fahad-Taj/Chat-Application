package com.example.redchat.chatPaging

import androidx.paging.Pager
import androidx.paging.PagingConfig

class ChatPager(val conversationId: String) {
    fun getChats() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { ChatPagingSource(conversationId) }
    )
}