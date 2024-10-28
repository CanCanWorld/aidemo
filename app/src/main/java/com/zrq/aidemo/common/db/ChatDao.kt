package com.zrq.aidemo.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chatEntity: ChatEntity)

    @Query("SELECT * FROM chat WHERE name = :name and is_config = 0")
    fun getChatsByAiName(name: String): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chat")
    fun getChats(): Flow<List<ChatEntity>>


    @Query("SELECT * FROM chat WHERE name = :name and is_config = 1")
    fun getConfigByAiName(name: String): Flow<List<ChatEntity>>

}