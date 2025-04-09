package com.sunchen.sunnyweather

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sunchen.sunnyweather.databinding.ActivityMainBinding
import com.sunchen.sunnyweather.ui.place.PlaceViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewmodel: PlaceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[PlaceViewModel::class.java]
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.places.collect {
                    val list = it.getOrNull()
                    if (list != null) {
                        for (place in list) {
                            Log.d("flow天气", place.name)
                        }
                    }
                }
            }
        }
        // viewmodel.placeLiveData.observe(this) { result ->
        //     val list = result.getOrNull()
        //     if (list != null) {
        //         for (place in list) {
        //             Log.d("livedata天气", place.name)
        //         }
        //     } else {
        //         Log.d("livedata天气", "空")
        //     }
        // }

        binding.btnTest.setOnClickListener {
            viewmodel.searchPlace("上海")
        }

    }
}