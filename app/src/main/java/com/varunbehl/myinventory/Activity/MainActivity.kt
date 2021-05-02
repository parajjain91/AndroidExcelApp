package com.varunbehl.myinventory.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.varunbehl.myinventory.DataAdapter
import com.varunbehl.myinventory.R
import com.varunbehl.myinventory.databinding.ActivityMainBinding
import com.varunbehl.myinventory.networkLayer.NetworkApiSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    val networkApiSource: NetworkApiSource by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        CoroutineScope(IO).launch {
            val excelResponse = networkApiSource.getMyApiResponse()
            print(excelResponse)
            withContext(Dispatchers.Main) {
                val recyclerViewAdapter = DataAdapter()
                binding.recyclerView.adapter = recyclerViewAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerViewAdapter.submitList(excelResponse.await().rows)
                recyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }
}