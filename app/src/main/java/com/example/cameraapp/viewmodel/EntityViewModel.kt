package com.example.cameraapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cameraapp.data.EntityTable
import com.example.cameraapp.repository.EntityRepository


class EntityViewModel(val repository: EntityRepository) : ViewModel(){

    /**
     * Getting the response from api
     * */
    fun addEntity(table: EntityTable){
        repository.addEntity(table)
    }

    /**
     * getting the list of ResponseEntity
     * */
    fun getEntity(): LiveData<List<EntityTable>> {
       return repository.getEntity()
    }



}