package com.example.redchat.chatPaging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.redchat.api.RetrofitInstance
import com.example.redchat.api.access_token
import com.example.redchat.models.loadChat.Message

class ChatPagingSource(val conversationId: String): PagingSource<Int, Message>() {
    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        return try{
            val position = params.key ?: 1
            val response = RetrofitInstance.api.loadChats(
                "Bearer $access_token",
                20,
                position,
                conversationId
            )
            LoadResult.Page(
                data = response.body()?.messages!!,
                prevKey = if(position == 1) null else position - 1,
                nextKey = if(response.body()?.hasMore == true) position + 1 else null
            )
        } catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}