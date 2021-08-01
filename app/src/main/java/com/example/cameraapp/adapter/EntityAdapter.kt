package com.example.cameraapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cameraapp.R
import com.example.cameraapp.data.EntityTable

class EntityAdapter(private var entity: List<EntityTable>) :
    RecyclerView.Adapter<EntityAdapter.EntityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.entity_item_layout, parent, false)
        return EntityViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {

        Glide.with(holder.view)
            .load(entity[position].image_url)
            .into(holder.mIvItemImage)

        holder.mTvItemDate.text = entity[position].time_stamp
        holder.mTvItemTime.text = entity[position].time_stamp


    }

    override fun getItemCount(): Int {
        return entity.size
    }


    class EntityViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val mIvItemImage: ImageView = view.findViewById(R.id.ivImage)
        val mTvItemDate: TextView = view.findViewById(R.id.tvDate)
        val mTvItemTime: TextView = view.findViewById(R.id.tvTime)

    }
}