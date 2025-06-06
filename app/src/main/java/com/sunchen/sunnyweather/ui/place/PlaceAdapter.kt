package com.sunchen.sunnyweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunchen.sunnyweather.R
import com.sunchen.sunnyweather.databinding.ItemPlaceBinding
import com.sunchen.sunnyweather.logic.model.Place
import com.sunchen.sunnyweather.ui.weather.WeatherActivity


/**
 * @CreateTime 2025-04-10 11:51
 *
 * @Author sunchen
 *
 * @Description
 */


class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceAdapter.ViewHolder {
        val bind = ItemPlaceBinding.bind(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_place, parent, false)
        )
        val viewHolder = ViewHolder(bind)
        // item点击事件
        viewHolder.itemView.setOnClickListener {
            val place = placeList[viewHolder.bindingAdapterPosition]
            fragment.viewModel.savePlace(place)
            WeatherActivity.startWeatherWithPlace(parent.context, place)
            fragment.activity?.finish()
        }


        return viewHolder
    }

    override fun onBindViewHolder(holder: PlaceAdapter.ViewHolder, position: Int) {
        holder.binding.tvPlaceName.text = placeList[position].name
        holder.binding.tvPlaceAddress.text = placeList[position].address

    }

    override fun getItemCount() = placeList.size
}