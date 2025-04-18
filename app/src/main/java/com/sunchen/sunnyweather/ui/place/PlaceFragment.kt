package com.sunchen.sunnyweather.ui.place

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunchen.sunnyweather.R
import com.sunchen.sunnyweather.databinding.FragmentPlaceBinding
import com.sunchen.sunnyweather.logic.model.Place
import com.sunchen.sunnyweather.ui.weather.WeatherActivity
import com.sunchen.sunnyweather.util.TAG
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

/**
 * @CreateTime 2025-04-10 14:08
 *
 * @Author sunchen
 *
 * @Description
 */

class PlaceFragment : Fragment() {

    private lateinit var binding: FragmentPlaceBinding
    private lateinit var adapter: PlaceAdapter

    val viewModel by lazy {
        ViewModelProvider(this)[PlaceViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentPlaceBinding.bind(inflater.inflate(R.layout.fragment_place, container, false))
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PlaceAdapter(this, viewModel.placeList)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.places.collect { result ->
                    result.onSuccess {
                        result.map { placeList ->
                            viewModel.placeList.clear()
                            viewModel.placeList.addAll(placeList)
                            // 更新
                            adapter.notifyDataSetChanged()
                        }
                    }

                    result.onFailure {
                        Log.d(TAG, it.message.toString())
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        }

        viewModel.search("上海")

        binding.edSearch.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.search(content)
            } else {
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }

        }

        val job = lifecycleScope.launch(start = CoroutineStart.LAZY) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPlace()
                    .collect { place ->
                        this@PlaceFragment.context?.let {
                            WeatherActivity.startWeatherWithPlace(
                                it, place
                            )
                        }
                        this@PlaceFragment.activity?.finish()
                    }
            }
        }

        //只有当有地址存储时，才开启订阅
        if (viewModel.isPlaceSaved()) {
            job.start()
        } else {
            job.cancel()
        }
    }

}