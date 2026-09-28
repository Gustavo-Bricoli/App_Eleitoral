package com.example.app_eleitoral

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val responses = AppStore.load(this)
        val root = Ui.root(this)
        val people = intent.getStringExtra("screen") == "people"
        root.addView(Ui.title(this, if (people) "Entrevistados" else "Resultado da pesquisa"))
        if (people) {
            if (responses.isEmpty()) addLine(root, "Nenhum entrevistado cadastrado.")
            responses.forEach {
                addLine(root, "${it.nome} - ${it.telefone}\n${it.data} | \n${it.horario} | ${it.localizacao}")
            }
        } else {
            addLine(root, "Quantidade de entrevistados: ${responses.size}")
            val counts = responses.groupingBy { it.candidato.ifBlank { "Não informado" } }.eachCount()
            addLine(root, if (counts.isEmpty()) "Ainda não há votos registrados." else counts.entries.joinToString("\n") { "${it.key}: ${it.value} voto(s)" })
            val problemCounts = responses.flatMap { it.problemas }.groupingBy { it }.eachCount()
            if (problemCounts.isNotEmpty()) {
                addLine(root, "\nProblemas mais citados:\n" + problemCounts.entries.sortedByDescending { it.value }
                    .joinToString("\n") { "${it.key}: ${it.value}" })
            }
        }
        root.addView(Ui.button(this, "Voltar") { finish() })
        setContentView(root)
    }

    private fun addLine(root: android.widget.LinearLayout, text: String) {
        root.addView(TextView(this).apply {
            this.text = text
            textSize = 16f
            setTextColor(Color.DKGRAY)
            setPadding(0, 8, 0, 14)
        })
    }
}
