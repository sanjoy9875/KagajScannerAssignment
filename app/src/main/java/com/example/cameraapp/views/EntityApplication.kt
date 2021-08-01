package com.example.cameraapp.views

import android.app.Application
import com.example.cameraapp.data.EntityDatabase
import com.example.cameraapp.repository.EntityRepository

class EntityApplication : Application() {

    private val entityDAO by lazy {
        val roomDatabase = EntityDatabase.getRoomDatabase(this)
        roomDatabase.getEntityDao()
    }

    val repository by lazy {
        EntityRepository(entityDAO)
    }

}