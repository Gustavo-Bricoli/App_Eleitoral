package com.example.app_eleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class ProblemsActivity : AppCompatActivity() {
    private val problems = listOf("Geração de emprego", "Saúde", "Asfaltamento e manutenção de vias", "Iluminação pública", "Lazer e cultura", "Meio ambiente", "Segurança", "Educação", "Transporte", "Custo de vida")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = Ui.root(this)
        root.addView(Ui.title(this, "Selecione até três problemas"))
        val checks = problems.map { label ->
            CheckBox(this).apply { text = label }
        }
        checks.forEach { check ->
            root.addView(check, android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                48
            ))
        }
        val other = EditText(this).apply { hint = "Outro problema (opcional)" }
        root.addView(other)
        root.addView(Ui.button(this, "Continuar") {
            val selected = checks.filter { it.isChecked }.map { it.text.toString() }.toMutableList()
            val otherValue = other.text.toString().trim()
            if (otherValue.isNotEmpty()) selected.add(otherValue)
            if (selected.size > 3) Ui.message(this, "Escolha no máximo três problemas.")
            else {
                SurveyDraft.problems = selected
                startActivity(Intent(this, IntervieweeActivity::class.java))
            }
        })
        setContentView(root)
    }
}
