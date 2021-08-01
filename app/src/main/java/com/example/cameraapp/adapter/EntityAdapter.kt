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

class EntityAdapter(private var entity: List<EntityTable>,private val onItemClick: OnItemClick) :
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

        holder.mTvItemName.text = entity[position].image_name
        holder.mTvItemDate.text = entity[position].date
        holder.mTvItemTime.text = entity[position].time

        holder.itemView.setOnClickListener {
            onItemClick.onEntityItemClicked(position,holder.mIvItemImage)
        }


    }

    override fun getItemCount(): Int {
        return entity.size
    }


    /**
     * ViewHolder class
     **/
    class EntityViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val mIvItemImage: ImageView = view.findViewById(R.id.ivImage)
        val mTvItemName: TextView = view.findViewById(R.id.tvImageName)
        val mTvItemDate: TextView = view.findViewById(R.id.tvDate)
        val mTvItemTime: TextView = view.findViewById(R.id.tvTime)

    }
}