package com.example.app_eleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ProblemasActivity : AppCompatActivity() {
    private val problemas = listOf(
        "Geração de emprego", "Saúde", "Asfaltamento e manutenção de vias",
        "Iluminação pública", "Lazer e cultura", "Meio ambiente",
        "Segurança", "Educação", "Transporte", "Custo de vida"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problemas)
        val container = findViewById<LinearLayout>(R.id.containerProblemas)
        val checks = problemas.map { label ->
            CheckBox(this).apply {
                text = label
                setTextColor(getColor(R.color.black))
                buttonTintList = getColorStateList(R.color.survey_green)
            }
        }
        checks.forEach(container::addView)

        val other = findViewById<EditText>(R.id.editOutroProblema)
        findViewById<android.widget.Button>(R.id.btnContinuarProblemas).setOnClickListener {
            val selected = checks.filter { it.isChecked }.map { it.text.toString() }.toMutableList()
            other.text.toString().trim().takeIf { it.isNotEmpty() }?.let(selected::add)
            if (selected.size > 3) {
                Ui.message(this, "Escolha no máximo três problemas.")
            } else {
                SurveyDraft.problemas = selected
                startActivity(Intent(this, activity_dadosentrevistado::class.java))
            }
        }
    }
}
