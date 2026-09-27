package com.example.app_eleitoral

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IntervieweeActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var phone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = Ui.root(this)
        root.addView(Ui.title(this, "Dados do entrevistado"))
        root.addView(android.widget.TextView(this).apply {
            text = getString(R.string.interviewee_audit_notice)
        })
        name = EditText(this).apply { hint = "Nome completo" }
        phone = EditText(this).apply { hint = "Celular"; inputType = 2 }
        root.addView(name)
        root.addView(phone)
        root.addView(Ui.button(this, "Finalizar pesquisa") { save() })
        setContentView(root)
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 10)
        }
    }

    private fun save() {
        val person = name.text.toString().trim()
        val cell = phone.text.toString().trim()
        if (person.isEmpty() || cell.isEmpty()) {
            Ui.message(this, "Preencha nome e celular.")
            return
        }
        val now = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        AppStore.save(this, SurveyResponse(person, cell, SurveyDraft.candidate, SurveyDraft.problems, now, locationText()))
        SurveyDraft.clear()
        Ui.message(this, "Pesquisa salva com sucesso.") 
        finishAffinity()
        startActivity(android.content.Intent(this, MainActivity::class.java))
    }

    private fun locationText(): String {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return "Permissão não concedida"
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        val location = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
            .asSequence()
            .filter { manager.isProviderEnabled(it) }
            .mapNotNull { manager.getLastKnownLocation(it) }
            .firstOrNull()
        return location?.let { "%.6f, %.6f".format(Locale.US, it.latitude, it.longitude) } ?: "Localização indisponível"
    }
}
