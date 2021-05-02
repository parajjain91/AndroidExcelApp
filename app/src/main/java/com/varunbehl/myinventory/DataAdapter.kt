package com.varunbehl.myinventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varunbehl.myinventory.databinding.ListItemExcelLayoutBinding
import com.varunbehl.myinventory.datamodel.MyExcelResponse

class DataAdapter : ListAdapter<MyExcelResponse.Row, DataAdapter.MyExcelResponseViewHolder>(Companion) {

    var dataBinding: ListItemExcelLayoutBinding? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyExcelResponseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        dataBinding = ListItemExcelLayoutBinding.inflate(layoutInflater)

        return MyExcelResponseViewHolder(dataBinding!!)
    }

    override fun onBindViewHolder(holder: MyExcelResponseViewHolder, position: Int) {
        val currentMyExcelResponse = getItem(position)
        dataBinding?.textView?.text = currentMyExcelResponse.itemname
    }

    class MyExcelResponseViewHolder(binding: ListItemExcelLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<MyExcelResponse.Row>() {
        override fun areItemsTheSame(oldItem: MyExcelResponse.Row, newItem: MyExcelResponse.Row): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(
            oldItem: MyExcelResponse.Row,
            newItem: MyExcelResponse.Row
        ): Boolean = oldItem.itemname == newItem.itemname
    }
}