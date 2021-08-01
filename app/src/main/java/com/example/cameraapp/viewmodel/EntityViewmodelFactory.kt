package com.example.cameraapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cameraapp.repository.EntityRepository

class EntityViewmodelFactory(val repository: EntityRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EntityViewModel(repository) as T
    }
}