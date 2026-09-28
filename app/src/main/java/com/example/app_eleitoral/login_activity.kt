package com.example.app_eleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class activity_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val Usuario = findViewById<EditText>(R.id.textusuario)
        val Senha = findViewById<EditText>(R.id.textsenha)
        val Entrar = findViewById<Button>(R.id.btentrar)
        val Encerrar = findViewById<Button>(R.id.btencerrar)

        Entrar.setOnClickListener {
            val user = Usuario.text.toString().trim()
            val pass = Senha.text.toString()

            if ((user == "admin" && pass == "admin") ||
                (user == "entrevistador" && pass == "entrevistador")) {
                startActivity(Intent(this, HomeActivity::class.java).putExtra("role", user))
                finish()
            } else {
                Ui.message(this, "Usuário ou senha inválidos.")
            }
        }

        Encerrar.setOnClickListener {
            finishAffinity()
        }
    }
}