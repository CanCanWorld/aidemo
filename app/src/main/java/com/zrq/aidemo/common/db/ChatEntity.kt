package com.zrq.aidemo.common.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class ChatEntity (
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "time")
    val time: Long,
    @ColumnInfo(name = "is_ai")
    val isAi: Int,
    @ColumnInfo(name = "is_config")
    val isConfig: Int,
    @ColumnInfo(name = "image")
    val image: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)