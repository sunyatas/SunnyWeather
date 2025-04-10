package com.sunchen.sunnyweather.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunchen.sunnyweather.R
import com.sunchen.sunnyweather.databinding.ItemPlaceBinding
import com.sunchen.sunnyweather.logic.model.Place


/**
 * @CreateTime 2025-04-10 11:51
 *
 * @Author sunchen
 *
 * @Description
 */


class PlaceAdapter(private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceAdapter.ViewHolder {
        val bind = ItemPlaceBinding.bind(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_place, parent, false)
        )
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(holder: PlaceAdapter.ViewHolder, position: Int) {
        holder.binding.tvPlaceName.text = placeList[position].name
        holder.binding.tvPlaceAddress.text = placeList[position].address
    }

    override fun getItemCount() = placeList.size
}