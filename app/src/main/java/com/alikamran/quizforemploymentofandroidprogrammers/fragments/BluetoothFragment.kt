package com.alikamran.quizforemploymentofandroidprogrammers.fragments

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alikamran.quizforemploymentofandroidprogrammers.R
import com.alikamran.quizforemploymentofandroidprogrammers.adapter.BluetoothRecyclerViewAdapter
import com.alikamran.quizforemploymentofandroidprogrammers.databinding.FragmentBluetoothBinding
import com.alikamran.quizforemploymentofandroidprogrammers.model.BluetoothModel
import com.alikamran.quizforemploymentofandroidprogrammers.reciver.MyBluetoothReceiver
import com.alikamran.quizforemploymentofandroidprogrammers.utils.ConstValue.BLUETOOTH_PERMISSION_REQUEST
import com.alikamran.quizforemploymentofandroidprogrammers.utils.ConstValue.REQUEST_ENABLE_BT
import com.alikamran.quizforemploymentofandroidprogrammers.viewmodel.BluetoothViewModel


class BluetoothFragment : Fragment(), MyBluetoothReceiver.OnBluetoothReceiver {


    private lateinit var receiver1: MyBluetoothReceiver
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var binding: FragmentBluetoothBinding
    private lateinit var viewModel: BluetoothViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_bluetooth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBluetoothBinding.bind(view)

        viewModel = ViewModelProvider(this)[BluetoothViewModel::class.java]

        bluetoothManager = requireActivity().getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        val adapter = BluetoothRecyclerViewAdapter()
        adapter.differ.submitList(null)
        binding.bluetoothRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }


        receiver1 = MyBluetoothReceiver()
        receiver1.addOnPermissionListener(this)

        if (hasBluetoothPermissions()) {
            enabledBluetooth()
        } else {
            requestBluetoothPermissions()
        }
        viewModel.bluetoothLiveData.observe(viewLifecycleOwner) { result ->
            adapter.differ.submitList(result)
        }

    }

    private fun enabledBluetooth() {


        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

        } else {
            discoverDevices()
        }
    }


    private fun hasBluetoothPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        } else {
            return (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.BLUETOOTH_ADMIN
            ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        }
    }

    private fun requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                BLUETOOTH_PERMISSION_REQUEST
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                BLUETOOTH_PERMISSION_REQUEST
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {

                discoverDevices()
            } else {
                Toast.makeText(requireContext(), "Please enable your bluetooth", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun discoverDevices() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        requireContext().registerReceiver(receiver1, filter)

        bluetoothAdapter?.startDiscovery()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == BLUETOOTH_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED
            ) {
                enabledBluetooth()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.unregisterReceiver(receiver1)
    }

    override fun onPermissionListener() {
        requestBluetoothPermissions()
    }

    override fun onAddBluetoothListener(bluetoothModel: BluetoothModel) {
        viewModel.addBluetooth(bluetoothModel)
    }

}