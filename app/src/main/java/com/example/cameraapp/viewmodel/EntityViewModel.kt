package com.example.cameraapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cameraapp.data.EntityTable
import com.example.cameraapp.repository.EntityRepository


class EntityViewModel(val repository: EntityRepository) : ViewModel(){

    /**
     * Adding the data into database
     **/

    fun addEntity(table: EntityTable){
        repository.addEntity(table)
    }

    /**
     * getting the list of EntityTable
     * */
    fun getEntity(): LiveData<List<EntityTable>> {
       return repository.getEntity()
    }



}