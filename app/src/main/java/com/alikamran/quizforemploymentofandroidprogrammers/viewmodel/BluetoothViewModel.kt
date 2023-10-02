package com.alikamran.quizforemploymentofandroidprogrammers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alikamran.quizforemploymentofandroidprogrammers.model.BluetoothModel

class BluetoothViewModel : ViewModel() {


    private val _bluetoothLiveData = MutableLiveData<List<BluetoothModel>>().apply {
        value = emptyList()
    }
    val bluetoothLiveData: LiveData<List<BluetoothModel>> = _bluetoothLiveData
    fun addBluetooth(bluetoothModel: BluetoothModel) {
        val currentList = _bluetoothLiveData.value?.toMutableList() ?: mutableListOf()

        if (!currentList.contains(bluetoothModel)) {
            currentList.add(bluetoothModel)
            _bluetoothLiveData.value = currentList
        }

    }

}