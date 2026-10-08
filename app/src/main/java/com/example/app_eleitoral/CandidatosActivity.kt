package com.example.app_eleitoral

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources

class CandidatosActivity : AppCompatActivity() {
    private data class Candidato(val nome: String, val partido: String, val foto: Int? = null)

    private val candidatos = listOf(
        Candidato("Duas-Caras", "Partido 1", R.drawable.duas_caras),
        Candidato("Pinguim", "Partido 2", R.drawable.pinguim),
        Candidato("Espantalho", "Partido 3", R.drawable.espantalho),
        Candidato("Bane", "Partido 4", R.drawable.bane),
        Candidato("Coringa", "Partido 5", R.drawable.coringa),
        Candidato("Branco", ""),
        Candidato("Nulo", ""),
        Candidato("Não sabe / não quer responder", "")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val stimulated = intent.getBooleanExtra("stimulated", false)
        setContentView(if (stimulated) R.layout.activity_pesquisa_estimulada
        else R.layout.activity_pesquisa_espontanea)

        val input = findViewById<EditText>(R.id.editRespostaEspontanea)
        if (stimulated) {
            input.visibility = View.GONE
            val group = findViewById<RadioGroup>(R.id.radioCandidatos)
            candidatos.forEach { candidato ->
                group.addView(RadioButton(this).apply {
                    text = if (candidato.partido.isBlank()) candidato.nome
                    else "${candidato.nome}\n${candidato.partido}"
                    setTextColor(getColor(R.color.black))
                    textSize = 16f
                    minHeight = 64
                    candidato.foto?.let { foto ->
                        val imagem: Drawable = requireNotNull(
                            AppCompatResources.getDrawable(this@CandidatosActivity, foto)
                        )
                        val tamanho = (64 * resources.displayMetrics.density).toInt()
                        imagem.setBounds(0, 0, tamanho, tamanho)
                        setCompoundDrawables(imagem, null, null, null)
                        compoundDrawablePadding = (12 * resources.displayMetrics.density).toInt()
                    }
                })
            }
            findViewById<android.widget.Button>(R.id.btnConfirmarCandidato).setOnClickListener {
                val selected = group.checkedRadioButtonId
                if (selected == -1) Ui.message(this, "Selecione uma opção.")
                else next(group.findViewById<RadioButton>(selected).text.toString().substringBefore("\n"), true)
            }
        } else {
            findViewById<android.widget.Button>(R.id.btnConfirmarCandidato).setOnClickListener {
                val value = input.text.toString().trim()
                if (value.isEmpty()) Ui.message(this, "Informe a resposta do entrevistado.")
                else next(value, false)
            }
        }
    }

    private fun next(candidato: String, stimulated: Boolean) {
        if (!stimulated) {
            SurveyDraft.candidatoEspontaneo = candidato
            SurveyDraft.mode = "Estimulada"
            startActivity(Intent(this, CandidatosActivity::class.java)
                .putExtra("stimulated", true))
        } else {
            SurveyDraft.candidato =
                "Espontânea: ${SurveyDraft.candidatoEspontaneo}; Estimulada: $candidato"
            startActivity(Intent(this, ProblemasActivity::class.java))
        }
    }
}
