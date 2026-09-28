package com.example.app_eleitoral

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SurveyTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = Ui.root(this)
        root.addView(Ui.title(this, "Escolha o tipo de pesquisa"))
        root.addView(Ui.button(this, "Espontânea") { open("Espontânea", false) })
        root.addView(Ui.button(this, "Estimulada") { open("Estimulada", true) })
        root.addView(Ui.button(this, "Problemas") {
            SurveyDraft.mode = "Problemas"
            startActivity(Intent(this, ProblemasActivity::class.java))
        })
        root.addView(Ui.button(this, "Seguir ordem completa") { open("Espontânea", false) })
        setContentView(root)
    }

    private fun open(mode: String, stimulated: Boolean) {
        SurveyDraft.mode = mode
        startActivity(Intent(this, CandidatosActivity::class.java).putExtra("stimulated", stimulated))
    }
}
