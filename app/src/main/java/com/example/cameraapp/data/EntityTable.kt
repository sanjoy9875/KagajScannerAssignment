package com.example.cameraapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity")
data class EntityTable(

    @ColumnInfo(name = "image_name")
    var image_name: String,

    @ColumnInfo(name = "image_url")
    var image_url: String,

    @ColumnInfo(name = "time_stamp")
    var time_stamp: String,


    ) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id:Int? = null

}