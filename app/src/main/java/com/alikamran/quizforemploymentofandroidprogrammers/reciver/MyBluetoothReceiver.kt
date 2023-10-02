package com.alikamran.quizforemploymentofandroidprogrammers.reciver

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.alikamran.quizforemploymentofandroidprogrammers.model.BluetoothModel

class MyBluetoothReceiver : BroadcastReceiver() {

    private var onBluetoothReceiver: OnBluetoothReceiver? = null


    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothDevice.ACTION_FOUND -> {

                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    onBluetoothReceiver?.onPermissionListener()
                }
                device?.name
                device?.address // MAC address
                Log.d("BluetoothDevice", "${device?.name}: ${device?.address}")
                onBluetoothReceiver?.onAddBluetoothListener(
                    BluetoothModel(
                        "${device?.name}",
                        "${device?.address}"
                    )
                )
            }

        }

    }

    fun addOnPermissionListener(onBluetoothReceiver: OnBluetoothReceiver) {

        this.onBluetoothReceiver = onBluetoothReceiver
    }

    interface OnBluetoothReceiver {
        fun onPermissionListener()
        fun onAddBluetoothListener(bluetoothModel: BluetoothModel)
    }
}