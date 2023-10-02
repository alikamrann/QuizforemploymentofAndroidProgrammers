package com.alikamran.quizforemploymentofandroidprogrammers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alikamran.quizforemploymentofandroidprogrammers.R
import com.alikamran.quizforemploymentofandroidprogrammers.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var binding :  FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        binding.bluetoothCv.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_bluetoothFragment)
        }
        binding.calculatorCv.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_calculatorFragment)
        }
        binding.memoryCv.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_memoryFragment)
        }
    }
}