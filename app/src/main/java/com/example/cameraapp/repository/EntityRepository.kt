package com.example.cameraapp.repository

import androidx.lifecycle.LiveData
import com.example.cameraapp.data.EntityDAO
import com.example.cameraapp.data.EntityTable


class EntityRepository(val entityDAO: EntityDAO) {

    /**
     * This function will store the data into database
     **/

    fun addEntity(table: EntityTable)  {
        entityDAO.addEntity(table)
    }

    /**
     * Give us the list of EntityTable
     **/
    fun getEntity(): LiveData<List<EntityTable>> {
        return entityDAO.getEntity()
    }



}