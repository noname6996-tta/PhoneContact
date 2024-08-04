package com.tta.phonebookapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tta.phonebookapplication.utils.COLUMN_EMAIL
import com.tta.phonebookapplication.utils.COLUMN_ID
import com.tta.phonebookapplication.utils.COLUMN_NAME
import com.tta.phonebookapplication.utils.COLUMN_PHONE
import com.tta.phonebookapplication.utils.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Contact(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Int?,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_EMAIL)
    val email: String,
    @ColumnInfo(name = COLUMN_PHONE)
    val phone: String
)