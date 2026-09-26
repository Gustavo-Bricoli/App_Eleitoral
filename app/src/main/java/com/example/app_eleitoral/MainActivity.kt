package com.example.app_eleitoral

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = Ui.root(this)
        root.addView(Ui.title(this, "Pesquisa Eleitoral 2026"))
        root.addView(TextView(this).apply {
            text = "Acesso ao sistema de coleta"
            textSize = 17f
            setTextColor(Color.DKGRAY)
        })
        root.addView(Ui.space(this))
        val user = EditText(this).apply { hint = "Usuário" }
        val password = EditText(this).apply {
            hint = "Senha"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        root.addView(user)
        root.addView(password)
        root.addView(Ui.space(this))
        root.addView(Ui.button(this, "Entrar") {
            val name = user.text.toString().trim()
            val pass = password.text.toString()
            if ((name == "admin" && pass == "admin") ||
                (name == "entrevistador" && pass == "entrevistador")) {
                startActivity(Intent(this, HomeActivity::class.java).putExtra("role", name))
                finish()
            } else {
                Ui.message(this, "Usuário ou senha inválidos.")
            }
        })
        root.addView(Ui.space(this, 20))
        root.addView(TextView(this).apply {
            text = "Usuários didáticos: admin / admin e entrevistador / entrevistador"
            textSize = 12f
            setTextColor(Color.GRAY)
        })
        setContentView(root)
    }
}
