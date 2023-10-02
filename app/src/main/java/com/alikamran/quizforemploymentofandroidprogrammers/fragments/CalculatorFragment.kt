package com.alikamran.quizforemploymentofandroidprogrammers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alikamran.quizforemploymentofandroidprogrammers.databinding.FragmentCalculatorBinding
import com.alikamran.quizforemploymentofandroidprogrammers.viewmodel.CalculatorViewModel


class CalculatorFragment : Fragment(), View.OnClickListener,ToastNotifier {

    private lateinit var binding: FragmentCalculatorBinding
    private lateinit var viewModel: CalculatorViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            com.alikamran.quizforemploymentofandroidprogrammers.R.layout.fragment_calculator,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalculatorBinding.bind(view)
        viewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]
        viewModel.setToastNotifier(this)

        viewModel.resultText.observe(viewLifecycleOwner) { result ->
            binding.textViewInputNumbers.text = result
        }
        setOnclickListeners()


    }

    private fun setOnclickListeners() {
        binding.buttonZero.setOnClickListener(this)
        binding.buttonOne.setOnClickListener(this)
        binding.buttonTwo.setOnClickListener(this)
        binding.buttonThree.setOnClickListener(this)
        binding.buttonFour.setOnClickListener(this)
        binding.buttonFive.setOnClickListener(this)
        binding.buttonSix.setOnClickListener(this)
        binding.buttonSeven.setOnClickListener(this)
        binding.buttonEight.setOnClickListener(this)
        binding.buttonNine.setOnClickListener(this)

        binding.buttonClear.setOnClickListener(this)
        binding.buttonParentheses.setOnClickListener(this)
        binding.buttonPercent.setOnClickListener(this)
        binding.buttonDivision.setOnClickListener(this)
        binding.buttonMultiplication.setOnClickListener(this)
        binding.buttonSubtraction.setOnClickListener(this)
        binding.buttonAddition.setOnClickListener(this)
        binding.buttonEqual.setOnClickListener(this)
        binding.buttonDot.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.buttonZero.id -> viewModel.onDigitButtonClick("0")
            binding.buttonOne.id -> viewModel.onDigitButtonClick("1")
            binding.buttonTwo.id -> viewModel.onDigitButtonClick("2")
            binding.buttonThree.id -> viewModel.onDigitButtonClick("3")
            binding.buttonFour.id -> viewModel.onDigitButtonClick("4")
            binding.buttonFive.id -> viewModel.onDigitButtonClick("5")
            binding.buttonSix.id -> viewModel.onDigitButtonClick("6")
            binding.buttonSeven.id -> viewModel.onDigitButtonClick("7")
            binding.buttonEight.id -> viewModel.onDigitButtonClick("8")
            binding.buttonNine.id -> viewModel.onDigitButtonClick("9")


            binding.buttonAddition.id -> viewModel.onOperatorButtonClick("+")
            binding.buttonSubtraction.id -> viewModel.onOperatorButtonClick("-")
            binding.buttonMultiplication.id -> viewModel.onOperatorButtonClick("x")
            binding.buttonDivision.id -> viewModel.onOperatorButtonClick("\\u00F7")
            binding.buttonPercent.id -> viewModel.onOperatorButtonClick("%")


            binding.buttonDot.id -> viewModel.onDotButtonClick()


            binding.buttonParentheses.id -> viewModel.onParenthesisButtonClick()

            binding.buttonClear.id -> {
                viewModel.onClearButtonClick()
            }

            binding.buttonEqual.id ->  viewModel.onEqualButtonClick()
        }
    }








    override fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}