package com.alikamran.quizforemploymentofandroidprogrammers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alikamran.quizforemploymentofandroidprogrammers.databinding.BluetoothItemLayoutBinding
import com.alikamran.quizforemploymentofandroidprogrammers.model.BluetoothModel


class BluetoothRecyclerViewAdapter :
    RecyclerView.Adapter<BluetoothRecyclerViewAdapter.BluetoothRecyclerViewViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<BluetoothModel>() {
        override fun areItemsTheSame(oldItem: BluetoothModel, newItem: BluetoothModel): Boolean {
            return oldItem.macAddress == newItem.macAddress

        }

        override fun areContentsTheSame(oldItem: BluetoothModel, newItem: BluetoothModel): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, callback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothRecyclerViewViewHolder {
        val binding = BluetoothItemLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BluetoothRecyclerViewViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BluetoothRecyclerViewViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
    }

    inner class BluetoothRecyclerViewViewHolder(private val binding: BluetoothItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bluetoothModel: BluetoothModel) {
            binding.item = bluetoothModel

            }

        }

    }



