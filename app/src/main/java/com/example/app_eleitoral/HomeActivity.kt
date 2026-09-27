package com.example.app_eleitoral

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    private val role get() = intent.getStringExtra("role") ?: "entrevistador"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = Ui.root(this)
        root.addView(Ui.title(this, if (role == "admin") "Painel do administrador" else "Painel do entrevistador"))
        root.addView(Ui.button(this, "Nova pesquisa") {
            SurveyDraft.clear()
            startActivity(Intent(this, SurveyTypeActivity::class.java))
        })
        if (role == "admin") {
            root.addView(Ui.button(this, "Visualizar resultados") {
                startActivity(Intent(this, AdminActivity::class.java).putExtra("screen", "results"))
            })
            root.addView(Ui.button(this, "Consultar entrevistados") {
                startActivity(Intent(this, AdminActivity::class.java).putExtra("screen", "people"))
            })
            root.addView(Ui.button(this, "Limpar dados da pesquisa") { confirmClear() })
        }
        root.addView(Ui.button(this, "Sair") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
        setContentView(root)
    }

    private fun confirmClear() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Limpar dados")
            .setMessage("Todos os entrevistados e resultados serão apagados.")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Limpar") { _, _ ->
                AppStore.clear(this)
                Ui.message(this, "Dados da pesquisa removidos.")
            }.show()
    }
}
