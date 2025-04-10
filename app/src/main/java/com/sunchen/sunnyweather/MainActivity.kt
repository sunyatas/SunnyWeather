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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
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
        //                         Log.d("flow天气成功", place.name)
        //                     }
        //                 }
        //             }
        //             result.onFailure {
        //                 Log.d("flow天气错误", it.message.toString())
        //             }
        //
        //         }
        //     }
        //
        // }
        // viewmodel.search("上海")


    }

}