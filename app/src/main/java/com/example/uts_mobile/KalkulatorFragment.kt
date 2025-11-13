package com.example.uts_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.uts_mobile.databinding.FragmentKalkulatorBinding
import kotlin.math.pow
import kotlin.math.sqrt

class KalkulatorFragment : Fragment() {

    private var _binding: FragmentKalkulatorBinding? = null
    private val binding get() = _binding!!

    private var ekspresi = ""
    private var sudahSelesaiHitung = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKalkulatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val angkaButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6,
            binding.btn7, binding.btn8, binding.btn9
        )

        angkaButtons.forEach { tombol ->
            tombol.setOnClickListener {
                if (sudahSelesaiHitung) {
                    ekspresi = ""
                    sudahSelesaiHitung = false
                }
                ekspresi += tombol.text
                updateExpression()
            }
        }

        binding.btnComma.setOnClickListener {
            if (sudahSelesaiHitung) ekspresi = ""
            if (ekspresi.isEmpty() || ekspresi.last() in "+-×÷") ekspresi += "0"
            val lastToken = lastToken(ekspresi)
            if (!lastToken.contains(".")) ekspresi += "."
            updateExpression()
        }

        binding.btnPlus.setOnClickListener { tambahAutoTutupAkarJikaPerlu(); tambahOperator("+") }
        binding.btnMinus.setOnClickListener {
            tambahAutoTutupAkarJikaPerlu()
            if (ekspresi.isEmpty() || sudahSelesaiHitung) {
                ekspresi = "-"
                sudahSelesaiHitung = false
            } else {
                tambahOperator("-")
            }
            updateExpression()
        }

        binding.btnMultiply.setOnClickListener { tambahAutoTutupAkarJikaPerlu(); tambahOperator("×") }
        binding.btnDivide.setOnClickListener { tambahAutoTutupAkarJikaPerlu(); tambahOperator("÷") }

        binding.btnSquare.setOnClickListener {
            if (ekspresi.isNotEmpty()) {
                ekspresi += "^2"
                updateExpression()
            }
        }

        binding.btnSqrt.setOnClickListener {
            if (sudahSelesaiHitung) ekspresi = ""
            if (ekspresi.isEmpty() || ekspresi.last() in "+-×÷") {
                ekspresi += "√("
            } else {
                ekspresi += "×√("
            }
            updateExpression()
        }

        binding.btnPercent.setOnClickListener {
            ekspresi += "%"
            updateExpression()
        }

        binding.btnSign.setOnClickListener {
            if (ekspresi.isNotEmpty()) {
                val last = ekspresi.last()

                if (last in "+-×÷." || last == '(') {
                    return@setOnClickListener
                }

                ekspresi = ubahTandaKonstekstual(ekspresi)
                updateExpression()
            }
        }

        binding.btnEqual.setOnClickListener {
            tambahAutoTutupAkarJikaPerlu()
            val hasil = evaluasiEkspresi(ekspresi)
            binding.tvExpression.text = "$ekspresi ="
            binding.tvDisplay.text = hasil
            ekspresi = hasil
            sudahSelesaiHitung = true
            updateAcButton()
        }

        binding.btnAC.setOnClickListener {
            if (sudahSelesaiHitung) {
                ekspresi = ""
                binding.tvDisplay.text = "0"
                binding.tvExpression.text = ""
                sudahSelesaiHitung = false
            } else if (ekspresi.isNotEmpty()) {
                ekspresi = ekspresi.dropLast(1)
            } else {
                ekspresi = ""
                binding.tvDisplay.text = "0"
                binding.tvExpression.text = ""
            }
            updateExpression()
        }

        binding.tvExpression.setOnClickListener {
            val ekspresiLama = binding.tvExpression.text.toString()
                .replace("=", "")
                .trim()
            if (ekspresiLama.isNotEmpty()) {
                ekspresi = ekspresiLama
                binding.tvDisplay.text = ekspresiLama
                binding.tvExpression.text = ""
                sudahSelesaiHitung = false
                updateAcButton()
            }
        }

        updateAcButton()
    }

    private fun tambahAutoTutupAkarJikaPerlu() {
        val buka = ekspresi.count { it == '(' }
        val tutup = ekspresi.count { it == ')' }
        if (buka > tutup) {
            repeat(buka - tutup) { ekspresi += ")" }
        }
    }

    private fun ubahTandaKonstekstual(expr: String): String {
        var e = expr.trim()

        if (e.endsWith(")")) {
            val openIdx = e.lastIndexOf("(-")
            if (openIdx != -1 && openIdx < e.length - 1) {
                e = e.removeRange(openIdx, openIdx + 2)
                e = e.removeSuffix(")")
                return e
            }
        }

        val idx = e.lastIndexOfAny(charArrayOf('+', '-', '×', '÷'))
        if (idx == -1) {
            return if (e.startsWith("-")) e.drop(1) else "-$e"
        }

        val operator = e[idx]
        val bagianAwal = e.substring(0, idx)
        val bagianAkhir = e.substring(idx + 1).trim()

        return when (operator) {
            '+' -> "$bagianAwal+${
                if (bagianAkhir.startsWith("(-")) bagianAkhir.removePrefix("(-").removeSuffix(")")
                else "(-$bagianAkhir)"
            }"
            '-' -> "$bagianAwal+$bagianAkhir"
            else -> e
        }
    }

    private fun lastToken(expr: String): String {
        if (expr.isEmpty()) return ""
        val idx = expr.lastIndexOfAny(charArrayOf('+', '-', '×', '÷'))
        return if (idx == -1) expr else expr.substring(idx + 1)
    }

    private fun tambahOperator(op: String) {
        if (ekspresi.isNotEmpty() && ekspresi.last() !in "+-×÷") {
            ekspresi += op
            updateExpression()
        }
    }

    private fun updateExpression() {
        binding.tvDisplay.text = if (ekspresi.isEmpty()) "0" else ekspresi
        updateAcButton()
    }

    private fun updateAcButton() {
        binding.btnAC.text = if (sudahSelesaiHitung || ekspresi.isEmpty()) "AC" else "⌫"
    }

    private fun evaluasiEkspresi(expr: String): String {
        return try {
            var exp = expr
                .replace("×", "*")
                .replace("÷", "/")
                .replace("%", "/100")
                .replace("√(", "sqrt(")
                .replace("(-", "(-1*")

            val buka = exp.count { it == '(' }
            val tutup = exp.count { it == ')' }
            if (buka > tutup) repeat(buka - tutup) { exp += ")" }

            val hasil = evaluasiString(exp)
            formatHasil(hasil)
        } catch (e: Exception) {
            "Error"
        }
    }

    private fun evaluasiString(expression: String): Double {
        val ekspresi = expression.replace("\\s+".toRegex(), "")
        return object {
            var pos = -1
            var ch = 0
            fun nextChar() { ch = if (++pos < ekspresi.length) ekspresi[pos].code else -1 }
            fun eat(c: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == c) { nextChar(); return true }
                return false
            }

            fun parse(): Double {
                nextChar(); val x = parseExpression()
                if (pos < ekspresi.length) throw RuntimeException("Unexpected: ${ch.toChar()}")
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    when {
                        eat('+'.code) -> x += parseTerm()
                        eat('-'.code) -> x -= parseTerm()
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    when {
                        eat('*'.code) -> x *= parseFactor()
                        eat('/'.code) -> x /= parseFactor()
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()
                var x: Double
                val start = pos
                if (eat('('.code)) {
                    x = parseExpression(); eat(')'.code)
                } else if ((ch in '0'.code..'9'.code) || ch == '.'.code) {
                    while ((ch in '0'.code..'9'.code) || ch == '.'.code) nextChar()
                    x = ekspresi.substring(start, pos).toDouble()
                } else if (ch in 'a'.code..'z'.code) {
                    while (ch in 'a'.code..'z'.code) nextChar()
                    val func = ekspresi.substring(start, pos)
                    x = parseFactor()
                    x = when (func) {
                        "sqrt" -> sqrt(x)
                        else -> throw RuntimeException("Unknown func: $func")
                    }
                } else throw RuntimeException("Unexpected: ${ch.toChar()}")
                if (eat('^'.code)) x = x.pow(parseFactor())
                return x
            }
        }.parse()
    }

    private fun formatHasil(hasil: Double): String {
        return if (hasil % 1 == 0.0) hasil.toInt().toString() else hasil.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}