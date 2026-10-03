package com.example.app_eleitoral

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale

class activity_dadosentrevistado : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dadosentrevistado)


        val assinaturaView = findViewById<AssinaturaView>(R.id.assinaturaView)
        val btlimpar = findViewById<Button>(R.id.btlimpar)
        val btconfirmar4 = findViewById<Button>(R.id.btconfirmar4)
        val btencerrar2 = findViewById<Button>(R.id.btencerrar2)

        val editNome = findViewById<EditText>(R.id.editNome)
        val editTelefone = findViewById<EditText>(R.id.editTelefone)

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 10)
        }

        btlimpar.setOnClickListener {
            assinaturaView.clear()
        }

        btconfirmar4.setOnClickListener {
            val nome = editNome.text.toString().trim()
            val telefone = editTelefone.text.toString().trim()

            if (nome.isEmpty() || telefone.isEmpty()) {
                Ui.message(this, "Preencha nome e celular.")
                return@setOnClickListener
            }

            if (!assinaturaView.isSigned()) {
                Ui.message(this, "Por favor, assine no quadro antes de continuar.")
                return@setOnClickListener
            }

            val bitmap = assinaturaView.getSignatureBitMap()
            val assinaturaTexto = ImageUtils.bitmapToBase64(bitmap)

            val resposta = SurveyResponse(
                nome = nome,
                telefone = telefone,
                candidato = SurveyDraft.candidato,
                problemas = SurveyDraft.problemas,
                data = LocalDate.now(),
                horario = LocalTime.now(),
                localizacao = locationText(),
                assinaturaBase64 = assinaturaTexto
            )

            AppStore.save(this, resposta)
            SurveyDraft.clear()

            Ui.message(this, "Pesquisa salva com sucesso!")
            finishAffinity()
            startActivity(Intent(this, activity_login::class.java))
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

    private fun locationText(): String {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "Permissão não concedida"
        }
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        val location = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
            .asSequence()
            .filter { manager.isProviderEnabled(it) }
            .mapNotNull { manager.getLastKnownLocation(it) }
            .firstOrNull()
        return location?.let { "%.6f, %.6f".format(Locale.US, it.latitude, it.longitude) } ?: "Localização indisponível"
    }
}