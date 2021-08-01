package com.example.cameraapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cameraapp.R
import com.example.cameraapp.adapter.EntityAdapter
import com.example.cameraapp.data.EntityTable
import com.example.cameraapp.viewmodel.EntityViewModel
import com.example.cameraapp.viewmodel.EntityViewmodelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: EntityAdapter
    private var entityList = mutableListOf<EntityTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = EntityAdapter(entityList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }
}