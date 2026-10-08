package com.example.app_eleitoral

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val responses = AppStore.load(this)

        setContentView(R.layout.activity_admin)
        val root = findViewById<LinearLayout>(R.id.containerAdmin)

        val people = intent.getStringExtra("screen") == "people"
        findViewById<android.widget.TextView>(R.id.textTituloAdmin).text =
            if (people) "Entrevistados" else "Resultado da pesquisa"

        if (people) {
            if (responses.isEmpty()) {
                addLine(root, "Nenhum entrevistado cadastrado.")
            } else {
                responses.forEach { it ->
                    addLine(root, "${it.nome} - ${it.telefone}\n${it.data} | ${it.horario} | ${it.localizacao}")

                    val bitmapAssinatura = it.assinaturaBase64.takeIf { base -> base.isNotEmpty() }?.let { base ->
                        ImageUtils.base64ToBitmap(base)
                    }

                    if (bitmapAssinatura != null) {
                        val imgAssinatura = ImageView(this).apply {
                            setImageBitmap(bitmapAssinatura)
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            adjustViewBounds = true
                            setBackgroundColor(Color.WHITE)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                70.dp
                            ).apply {
                                bottomMargin = 12.dp
                            }
                        }
                        root.addView(imgAssinatura)
                    }

                    root.addView(Ui.space(this, 8))
                }
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

        findViewById<android.widget.Button>(R.id.btnVoltarAdmin).setOnClickListener { finish() }
        if (!people) {
            findViewById<android.widget.Button>(R.id.btnGraficosAdmin).visibility = android.view.View.VISIBLE
            findViewById<android.widget.Button>(R.id.btnGraficosAdmin).setOnClickListener {
                startActivity(Intent(this, ChartsActivity::class.java))
            }
        }
    }

    private fun addLine(root: LinearLayout, text: String) {
        root.addView(TextView(this).apply {
            this.text = text
            textSize = 16f
            setTextColor(Color.DKGRAY)
            setPadding(0, 8.dp, 0, 4.dp)
        })
    }
}