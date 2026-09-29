package com.example.app_eleitoral

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_dadosentrevistado : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dadosentrevistado)

        val assinaturaView = findViewById<AssinaturaView>(R.id.assinaturaView)
        val btlimpar = findViewById<Button>(R.id.btlimpar)
        val btconfirmar4 = findViewById<Button>(R.id.btconfirmar4)
        val btencerrar2 = findViewById<Button>(R.id.btencerrar2)

        btlimpar.setOnClickListener {
            assinaturaView.clear()
        }

        btconfirmar4.setOnClickListener {
            if(!assinaturaView.isSigned()){
                Ui.message(this, "Por favor, assine no quadro antes de continuar.")
                return@setOnClickListener
            }
            val bitmap = assinaturaView.getSignatureBitMap()
        }
        btencerrar2.setOnClickListener {
            finishAffinity()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}