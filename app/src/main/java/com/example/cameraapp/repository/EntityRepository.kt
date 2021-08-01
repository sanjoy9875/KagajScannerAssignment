package com.example.cameraapp.repository

import androidx.lifecycle.LiveData
import com.example.cameraapp.data.EntityDAO
import com.example.cameraapp.data.EntityTable


class EntityRepository(val entityDAO: EntityDAO) {

    private var databaseList = mutableListOf<EntityTable>()

    /**
     * This function will call our API & give us the response
     * */
    fun addEntity(table: EntityTable)  {
        entityDAO.addEntity(table)
    }

    /**
     * Give us the list of ResponseEntity
     **/
    fun getEntity(): LiveData<List<EntityTable>> {
        return entityDAO.getEntity()
    }



}