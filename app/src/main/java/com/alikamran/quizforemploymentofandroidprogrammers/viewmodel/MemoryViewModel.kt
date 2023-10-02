package com.alikamran.quizforemploymentofandroidprogrammers.viewmodel

import android.os.Environment
import android.os.StatFs
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alikamran.quizforemploymentofandroidprogrammers.model.MemoryModel

class MemoryViewModel : ViewModel() {
    private val _memoryMutableLiveData = MutableLiveData<MemoryModel>()
    val memoryMutableLiveData: LiveData<MemoryModel> = _memoryMutableLiveData

    fun getFreeSize(){
        val memoryModel  = MemoryModel(getAvailableInternalMemorySize(),getTotalInternalMemorySize())
        _memoryMutableLiveData.value = memoryModel
    }
    private fun getAvailableInternalMemorySize(): Double {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return formatSize((availableBlocks * blockSize).toDouble())
    }

    private fun getTotalInternalMemorySize(): Double {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return formatSize((totalBlocks * blockSize).toDouble())
    }


    private fun formatSize(size: Double): Double {
        var size = size
        var suffix: String? = null
        if (size >= 1024) {
            suffix = "KB"
            size /= 1024
            if (size >= 1024) {
                suffix = "MB"
                size /= 1024
            }
        }
        if (suffix.equals("KB"))
            size/=1000

        return size
    }
}