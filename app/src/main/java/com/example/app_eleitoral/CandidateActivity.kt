package com.example.app_eleitoral

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources

class CandidateActivity : AppCompatActivity() {
    private data class Candidate(
        val name: String,
        val party: String,
        val photo: Int? = null
    )

    // Troque os nomes, partidos e imagens desta lista quando definir os candidatos reais.
    private val candidates = listOf(
        Candidate("Duas-Caras", "Partido 1", R.drawable.duas_caras),
        Candidate("Pinguim", "Partido 2", R.drawable.pinguim),
        Candidate("Espantalho", "Partido 3", R.drawable.espantalho),
        Candidate("Bane", "Partido 4", R.drawable.bane),
        Candidate("Coringa", "Partido 5", R.drawable.coringa),
        Candidate("Branco", ""),
        Candidate("Nulo", ""),
        Candidate("Não sabe / não quer responder", "")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = Ui.root(this)
        root.addView(Ui.title(this, "Intenção de voto - ${SurveyDraft.mode}"))
        val group = RadioGroup(this)
        group.orientation = RadioGroup.VERTICAL
        val stimulated = intent.getBooleanExtra("stimulated", false)
        if (stimulated) {
            candidates.forEach { candidate ->
                group.addView(RadioButton(this).apply {
                    text = if (candidate.party.isBlank()) candidate.name
                    else "${candidate.name}\n${candidate.party}"
                    textSize = 16f
                    minHeight = 76
                    setPadding(8, 8, 8, 8)
                    candidate.photo?.let { photo ->
                        val image: Drawable = requireNotNull(AppCompatResources.getDrawable(this@CandidateActivity, photo))
                        val size = (64 * resources.displayMetrics.density).toInt()
                        image.setBounds(0, 0, size, size)
                        setCompoundDrawables(image, null, null, null)
                        compoundDrawablePadding = (12 * resources.displayMetrics.density).toInt()
                    }
                }) 
            }
        } else {
            val input = android.widget.EditText(this)
            input.hint = "Nome do candidato (resposta espontânea)"
            root.addView(input)
            root.addView(Ui.button(this, "Confirmar candidato") {
                val value = input.text.toString().trim()
                if (value.isEmpty()) Ui.message(this, "Informe a resposta do entrevistado.")
                else next(value)
            })
            setContentView(root)
            return
        }
        root.addView(group)
        root.addView(Ui.button(this, "Confirmar candidato") {
            val selected = group.checkedRadioButtonId
            if (selected == -1) Ui.message(this, "Selecione uma opção.")
            else next(group.findViewById<RadioButton>(selected).text.toString().substringBefore("\n"))
        })
        setContentView(root)
    }

    private fun next(candidate: String) {
        SurveyDraft.candidate = candidate
        startActivity(Intent(this, ProblemsActivity::class.java))
    }
}
