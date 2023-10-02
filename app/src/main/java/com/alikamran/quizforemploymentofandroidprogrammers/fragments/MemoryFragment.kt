package com.alikamran.quizforemploymentofandroidprogrammers.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alikamran.quizforemploymentofandroidprogrammers.R
import com.alikamran.quizforemploymentofandroidprogrammers.databinding.FragmentMemoryBinding
import com.alikamran.quizforemploymentofandroidprogrammers.viewmodel.MemoryViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF


class MemoryFragment : Fragment() {

    private lateinit var binding: FragmentMemoryBinding
    private lateinit var viewModel: MemoryViewModel
    private val entries: ArrayList<PieEntry> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_memory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMemoryBinding.bind(view)
        viewModel = ViewModelProvider(this)[MemoryViewModel::class.java]
        viewModel.getFreeSize()

        viewModel.memoryMutableLiveData.observe(viewLifecycleOwner){result->
            binding.freeSizeTxt.text = "Free size: ${String.format("%.3f", result.freeSize)} MB"
            binding.totalSizeTxt.text = "Total size: ${String.format("%.3f", result.totalSize)} MB"

        }

        initializePieChart()


    }
    private fun initializePieChart(){
        settingUserPercentValue()


        settingDrag()


        settingHole()


        settingCircleColor()


        settingHoleRadius()


        settingCenterText()


        settingRotation()

        enableRotation()


        settingAnimation()


        disablingOurLegend()


        setData()


        val dataSet = PieDataSet(entries, "Free Storage")

        settingSlice(dataSet)

        val colors: ArrayList<Int> = addColors()
        dataSet.colors = colors
        settingPieData(dataSet)
        binding.memoryPieChart.highlightValues(null)

        // loading chart
        binding.memoryPieChart.invalidate()
    }

    private fun settingPieData(dataSet: PieDataSet) {
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        binding.memoryPieChart.data = data
    }

    private fun addColors(): ArrayList<Int> {
        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.GREEN)
        colors.add(Color.RED)
        return colors
    }

    private fun settingSlice(dataSet: PieDataSet) {
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
    }

    private fun setData() {
        entries.add(PieEntry(viewModel.memoryMutableLiveData.value?.freeSize!!.toFloat()))
        entries.add(
            PieEntry(
                viewModel.memoryMutableLiveData.value?.totalSize!!.toFloat()
                        - viewModel.memoryMutableLiveData.value?.freeSize!!.toFloat()
            )
        )
    }

    private fun disablingOurLegend() {
        binding.memoryPieChart.legend.isEnabled = false
        binding.memoryPieChart.setEntryLabelColor(Color.WHITE)
        binding.memoryPieChart.setEntryLabelTextSize(12f)
    }

    private fun settingAnimation() {
        binding.memoryPieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    private fun enableRotation() {
        binding.memoryPieChart.isRotationEnabled = true
        binding.memoryPieChart.isHighlightPerTapEnabled = true
    }

    private fun settingRotation() {
        binding.memoryPieChart.rotationAngle = 0f
    }



    private fun settingCenterText() {
        binding.memoryPieChart.setDrawCenterText(true)
    }

    private fun settingHoleRadius() {
        binding.memoryPieChart.holeRadius = 58f
        binding.memoryPieChart.transparentCircleRadius = 61f
    }

    private fun settingCircleColor() {
        binding.memoryPieChart.setTransparentCircleColor(Color.WHITE)
        binding.memoryPieChart.setTransparentCircleAlpha(110)
    }

    private fun settingHole() {
        binding.memoryPieChart.isDrawHoleEnabled = true
        binding.memoryPieChart.setHoleColor(Color.WHITE)
    }

    private fun settingDrag() {
        binding.memoryPieChart.dragDecelerationFrictionCoef = 0.95f
    }

    private fun settingUserPercentValue() {
        binding.memoryPieChart.setUsePercentValues(false)
        binding.memoryPieChart.description.isEnabled = false
        binding.memoryPieChart.setExtraOffsets(5f, 10f, 5f, 5f)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFreeSize()
    }
}