package com.sunchen.sunnyweather

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.color.MaterialColors
import com.sunchen.sunnyweather.databinding.ActivityMainBinding
import com.sunchen.sunnyweather.ui.place.PlaceViewModel
import com.sunchen.sunnyweather.util.getColorFromAttr
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val colorPrimary = getColorFromAttr(androidx.appcompat.R.attr.colorPrimary)

        this.enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(colorPrimary, colorPrimary))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // lifecycleScope.launch {
        //     repeatOnLifecycle(Lifecycle.State.STARTED) {
        //         viewmodel.places.collect { result ->
        //             result.onSuccess {
        //                 val list = result.getOrNull()
        //                 if (list != null) {
        //                     for (place in list) {
        //                         Log.d(TAG, place.name)
        //                     }
        //                 }
        //             }
        //             result.onFailure {
        //                 Log.d(TAG, it.message.toString())
        //             }
        //
        //         }
        //     }
        //
        // }
        // viewmodel.search("上海")


    }

}