package com.example.app_eleitoral

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity

class ProblemasActivity : AppCompatActivity() {
    private val problemas = listOf(
        "Geração de emprego", "Saúde", "Asfaltamento e manutenção de vias",
        "Iluminação pública", "Lazer e cultura", "Meio ambiente",
        "Segurança", "Educação", "Transporte", "Custo de vida"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scrollView = ScrollView(this).apply {
            isFillViewport = true
        }

        val root = Ui.root(this)

        val logo = ImageView(this).apply {
            setImageResource(R.drawable.eleitoral)
            scaleType = ImageView.ScaleType.FIT_CENTER
            adjustViewBounds = true
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                180
            ).apply {
                topMargin = 16
                bottomMargin = 16
            }
        }
        root.addView(logo)

        root.addView(Ui.title(this, "Selecione até três problemas"))

        val checks = problemas.map { label ->
            CheckBox(this).apply {
                text = label
                setTextColor(Color.BLACK)
            }
        }
        checks.forEach { check ->
            root.addView(check, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ))
        }

        val other = EditText(this).apply {
            hint = "Outro problema (opcional)"
            setTextColor(Color.BLACK)
            setHintTextColor(Color.DKGRAY)
        }
        root.addView(other)

        val btnContinuar = Ui.button(this, "Continuar") {
            val selected = checks.filter { it.isChecked }.map { it.text.toString() }.toMutableList()
            val otherValue = other.text.toString().trim()
            if (otherValue.isNotEmpty()) selected.add(otherValue)

            if (selected.size > 3) {
                Ui.message(this, "Escolha no máximo três problemas.")
            } else {
                SurveyDraft.problemas = selected
                startActivity(Intent(this, activity_dadosentrevistado::class.java))
            }
        }.apply {
            minHeight = 140
            setPadding(32, 24, 32, 24)
            textSize = 16f
        }

        val paramsBotao = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 24
            bottomMargin = 48
        }

        root.addView(btnContinuar, paramsBotao)

        scrollView.addView(root)
        setContentView(scrollView)
    }
}