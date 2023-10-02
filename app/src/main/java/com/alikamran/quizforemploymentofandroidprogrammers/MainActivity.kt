package com.alikamran.quizforemploymentofandroidprogrammers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alikamran.quizforemploymentofandroidprogrammers.viewmodel.CalculatorViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var calculatorViewModel: CalculatorViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculatorViewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]



    }


}