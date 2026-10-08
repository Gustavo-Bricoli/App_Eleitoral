package com.example.app_eleitoral

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    private val role get() = intent.getStringExtra("role") ?: "entrevistador"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        findViewById<android.widget.TextView>(R.id.textTituloHome).text =
            if (role == "admin") "Painel do administrador" else "Painel do entrevistador"
        findViewById<View>(R.id.btnResultados).visibility =
            if (role == "admin") View.VISIBLE else View.GONE
        findViewById<View>(R.id.btnEntrevistados).visibility =
            if (role == "admin") View.VISIBLE else View.GONE
        findViewById<View>(R.id.btnLimparDados).visibility =
            if (role == "admin") View.VISIBLE else View.GONE
        findViewById<View>(R.id.btnNovaPesquisa).setOnClickListener {
            SurveyDraft.clear()
            startActivity(Intent(this, SurveyTypeActivity::class.java))
        }
        findViewById<View>(R.id.btnResultados).setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java).putExtra("screen", "results"))
        }
        findViewById<View>(R.id.btnEntrevistados).setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java).putExtra("screen", "people"))
        }
        findViewById<View>(R.id.btnLimparDados).setOnClickListener { confirmClear() }
        findViewById<View>(R.id.btnSair).setOnClickListener {
            startActivity(Intent(this, activity_login::class.java))
            finish()
        }
    }

    private fun confirmClear() {
        AlertDialog.Builder(this)
            .setTitle("Limpar dados")
            .setMessage("Todos os entrevistados e resultados serão apagados.")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Limpar") { _, _ ->
                AppStore.clear(this)
                Ui.message(this, "Dados da pesquisa removidos.")
            }.show()
    }
}
