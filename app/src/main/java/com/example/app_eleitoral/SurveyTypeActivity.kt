package com.example.app_eleitoral

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SurveyTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_type)
        findViewById<android.widget.Button>(R.id.btnIniciarPesquisa).setOnClickListener {
            SurveyDraft.clear()
            SurveyDraft.mode = "Espontânea"
            startActivity(android.content.Intent(this, CandidatosActivity::class.java)
                .putExtra("stimulated", false))
        }
    }
}
