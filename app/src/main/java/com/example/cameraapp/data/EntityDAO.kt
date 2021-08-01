package com.example.cameraapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface EntityDAO {

    /**
     * This function add list of item into our Database
     * */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun addEntity(entityTable: EntityTable)

    /**
     * This function fetch the list of item from our Database
     * */
    @Query("select * from entity")
    fun getEntity(): LiveData<List<EntityTable>>

}