package com.zrq.aidemo.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertChat(chatEntity: ChatEntity)

    @Query("SELECT * FROM chat WHERE name = :name and is_config = 'false'")
    fun getChatsByAiName(name: String): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chat WHERE name = :name and is_config = 'true'")
    fun getConfigByAiName(name: String): Flow<List<ChatEntity>>

}