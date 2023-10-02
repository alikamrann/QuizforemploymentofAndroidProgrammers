package com.alikamran.quizforemploymentofandroidprogrammers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alikamran.quizforemploymentofandroidprogrammers.fragments.ToastNotifier
import com.alikamran.quizforemploymentofandroidprogrammers.utils.ConstValue
import com.alikamran.quizforemploymentofandroidprogrammers.utils.ConstValue.IS_CLOSE_PARENTHESIS
import com.alikamran.quizforemploymentofandroidprogrammers.utils.ConstValue.IS_DOT
import com.alikamran.quizforemploymentofandroidprogrammers.utils.ConstValue.IS_NUMBER
import com.alikamran.quizforemploymentofandroidprogrammers.utils.ConstValue.IS_OPEN_PARENTHESIS
import com.alikamran.quizforemploymentofandroidprogrammers.utils.ConstValue.IS_OPERAND
import java.math.BigDecimal
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class CalculatorViewModel : ViewModel() {
    private val _resultText = MutableLiveData<String>().apply {
        value = ""
    }
    val resultText: LiveData<String> = _resultText

    private var openParenthesis = 0
    private var lastExpression = ""
    private var dotUsed = false
    private var equalClicked = false
    private var toastNotifier: ToastNotifier? = null
    private val scriptEngine: ScriptEngine = ScriptEngineManager().getEngineByName("rhino")

    fun setToastNotifier(notifier: ToastNotifier) {
        toastNotifier = notifier
    }

    fun onDigitButtonClick(digit: String) {
        var done = false
        val operationLength: Int = _resultText.value.toString().length
        if (operationLength > 0) {
            val lastCharacter: String =
                _resultText.value.toString()[operationLength - 1] + ""
            val lastCharacterState = defineLastCharacter(lastCharacter)
            if (operationLength == 1 && lastCharacterState == IS_NUMBER && lastCharacter == "0") {
                _resultText.value = digit
                done = true
            } else if (lastCharacterState == IS_OPEN_PARENTHESIS) {
                _resultText.value = "${_resultText.value}$digit"
                done = true
            } else if (lastCharacterState == IS_CLOSE_PARENTHESIS || lastCharacter == "%") {
                _resultText.value = "${_resultText.value} x+ $digit"

                done = true
            } else if (lastCharacterState == IS_NUMBER || lastCharacterState == IS_OPERAND || lastCharacterState == IS_DOT) {
                _resultText.value = "${_resultText.value}$digit"

                done = true
            }
        } else {
            _resultText.value = "${_resultText.value}$digit"
            done = true
        }
        equalClicked = done
    }

    fun onOperatorButtonClick(operator: String) {
        var done = false
        val operationLength: Int = _resultText.value.toString().length
        if (operationLength > 0) {
            val lastInput: String = _resultText.value.toString()[operationLength - 1] + ""
            if (lastInput == "+" || lastInput == "-" || lastInput == "*" || lastInput == "\u00F7" || lastInput == "%") {
                toastNotifier?.showToast("Wrong format")
            } else if (operator == "%" && defineLastCharacter(lastInput) == IS_NUMBER) {
                _resultText.value = "${_resultText.value}$operator"
                ConstValue.dotUsed = false
                ConstValue.equalClicked = false
                ConstValue.lastExpression = ""
                done = true
            } else if (operator != "%") {
                _resultText.value = "${_resultText.value}$operator"
                ConstValue.dotUsed = false
                ConstValue.equalClicked = false
                ConstValue.lastExpression = ""
                done = true
            }
        } else {
            toastNotifier?.showToast("Wrong Format. Operand Without any numbers?")

        }
        equalClicked = done
    }

    fun onEqualButtonClick() {
        if (_resultText.value != "") {
            calculate(_resultText.value.toString())
        }
    }

    fun onClearButtonClick() {
        _resultText.value = ""
        openParenthesis = 0
        dotUsed = false
        equalClicked = false
        lastExpression = ""
    }


    private fun calculate(input: String) {
        var result: String
        try {
            var temp = input
            if (ConstValue.equalClicked) {
                temp = input + ConstValue.lastExpression
            } else {
                saveLastExpression(input)
            }
            result = scriptEngine.eval(
                temp.replace("%".toRegex(), "/100").replace("x".toRegex(), "*")
                    .replace("[^\\x00-\\x7F]".toRegex(), "/")
            ).toString()
            val decimal = BigDecimal(result)
            result = decimal.setScale(8, BigDecimal.ROUND_HALF_UP).toPlainString()
            ConstValue.equalClicked = true
        } catch (e: Exception) {
            toastNotifier?.showToast("Wrong Format")
            return
        }
        if (result == "Infinity") {
            toastNotifier?.showToast("Division by zero is not allowed")
            _resultText.value = input
        } else if (result.contains(".")) {
            result = result.replace("\\.?0*$".toRegex(), "")
            _resultText.value = result
        }
    }

    private fun defineLastCharacter(lastCharacter: String): Int {
        try {
            lastCharacter.toInt()
            return IS_NUMBER
        } catch (_: NumberFormatException) {
        }
        if (lastCharacter == "+" || lastCharacter == "-" || lastCharacter == "x" || lastCharacter == "\u00F7" || lastCharacter == "%") return IS_OPERAND
        if (lastCharacter == "(") return IS_OPEN_PARENTHESIS
        if (lastCharacter == ")") return IS_CLOSE_PARENTHESIS
        return if (lastCharacter == ".") IS_DOT else -1
    }

    fun onDotButtonClick() {
        var done = false
        if (_resultText.value.toString().length === 0) {
            _resultText.value = "0."
            ConstValue.dotUsed = true
            done = true
        } else if (defineLastCharacter(
                _resultText.value.toString()[_resultText.value.toString().length - 1] + ""
            ) == IS_OPERAND
        ) {
            _resultText.value = "${_resultText.value} 0."
            done = true
            ConstValue.dotUsed = true
        } else if (defineLastCharacter(
                _resultText.value.toString()[_resultText.value.toString().length - 1] + ""
            ) == IS_NUMBER
        ) {
            _resultText.value = "${_resultText.value}."
            done = true
            ConstValue.dotUsed = true
        }
        equalClicked = done
    }

    fun onParenthesisButtonClick() {
        var done = false
        val operationLength: Int = _resultText.value.toString().length
        if (operationLength == 0) {
            _resultText.value = "${_resultText.value} ("
            ConstValue.dotUsed = false
            openParenthesis++
            done = true
        } else if (openParenthesis > 0 && operationLength > 0) {
            val lastInput: String = _resultText.value.toString()[operationLength - 1] + ""
            when (defineLastCharacter(lastInput)) {
                IS_NUMBER -> {
                    _resultText.value = "${_resultText.value} )"
                    done = true
                    openParenthesis--
                    ConstValue.dotUsed = false
                }

                IS_OPERAND -> {
                    _resultText.value = "${_resultText.value} ("
                    done = true
                    openParenthesis++
                    ConstValue.dotUsed = false
                }

                IS_OPEN_PARENTHESIS -> {
                    _resultText.value = "${_resultText.value}("
                    done = true
                    openParenthesis++
                    ConstValue.dotUsed = false
                }

                IS_CLOSE_PARENTHESIS -> {
                    _resultText.value = "${_resultText.value} )"
                    done = true
                    openParenthesis--
                    ConstValue.dotUsed = false
                }
            }
        } else if (openParenthesis == 0 && operationLength > 0) {
            val lastInput: String = _resultText.value.toString()[operationLength - 1] + ""
            if (defineLastCharacter(lastInput) == IS_OPERAND) {
                _resultText.value = "${_resultText.value}("
                done = true
                ConstValue.dotUsed = false
                openParenthesis++
            } else {
                _resultText.value = "${_resultText.value}x("
                done = true
                ConstValue.dotUsed = false
                openParenthesis++
            }
        }
        equalClicked = done
    }

    private fun saveLastExpression(input: String) {
        val lastOfExpression = input[input.length - 1].toString() + ""
        if (input.length > 1) {
            if (lastOfExpression == ")") {
                ConstValue.lastExpression = ")"
                var numberOfCloseParenthesis = 1
                for (i in input.length - 2 downTo 0) {
                    if (numberOfCloseParenthesis > 0) {
                        val last = input[i].toString() + ""
                        if (last == ")") {
                            numberOfCloseParenthesis++
                        } else if (last == "(") {
                            numberOfCloseParenthesis--
                        }
                        ConstValue.lastExpression = last + ConstValue.lastExpression
                    } else if (defineLastCharacter(input[i].toString() + "") == IS_OPERAND) {
                        ConstValue.lastExpression = input[i] + ConstValue.lastExpression
                        break
                    } else {
                        ConstValue.lastExpression = ""
                    }
                }
            } else if (defineLastCharacter(lastOfExpression + "") == IS_NUMBER) {
                ConstValue.lastExpression = lastOfExpression
                for (i in input.length - 2 downTo 0) {
                    val last = input[i].toString() + ""
                    if (defineLastCharacter(last) == IS_NUMBER || defineLastCharacter(last) == IS_DOT) {
                        ConstValue.lastExpression = last + ConstValue.lastExpression
                    } else if (defineLastCharacter(last) == IS_OPERAND) {
                        ConstValue.lastExpression = last + ConstValue.lastExpression
                        break
                    }
                    if (i == 0) {
                        ConstValue.lastExpression = ""
                    }
                }
            }
        }
    }
}