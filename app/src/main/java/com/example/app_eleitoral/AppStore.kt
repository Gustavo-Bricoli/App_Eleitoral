package com.example.app_eleitoral

import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONArray

data class SurveyResponse(
    val nome: String,
    val telefone: String,
    val candidato: String,
    val problemas: List<String>,
    val data: String,
    val localizacao: String
)

object SurveyDraft {
    var candidato: String = ""
    var problemas: List<String> = emptyList()
    var mode: String = "Espontânea"

    fun clear() {
        candidato = ""
        problemas = emptyList()
        mode = "Espontânea"
    }
}

object AppStore {
    private const val DATABASE_NAME = "pesquisa_eleitoral.db"
    private const val DATABASE_VERSION = 1
    private const val TABLE_RESPONSES = "responses"

    private class Database(context: Context) : SQLiteOpenHelper(
        context.applicationContext,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE $TABLE_RESPONSES (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    telefone TEXT NOT NULL,
                    candidato TEXT NOT NULL,
                    problemas TEXT NOT NULL,
                    data TEXT NOT NULL,
                    localizacao TEXT NOT NULL
                )
                """.trimIndent()
            )
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // Database version is kept for future schema migrations.
        }
    }

    fun save(context: Context, response: SurveyResponse) {
        Database(context).use { helper ->
            val values = ContentValues().apply {
                put("nome", response.nome)
                put("telefone", response.telefone)
                put("candidato", response.candidato)
                put("problemas", JSONArray(response.problemas).toString())
                put("data", response.data)
                put("localizacao", response.localizacao)
            }
            helper.writableDatabase.insertOrThrow(TABLE_RESPONSES, null, values)
        }
    }

    fun load(context: Context): List<SurveyResponse> {
        Database(context).use { helper ->
            helper.readableDatabase.query(
                TABLE_RESPONSES,
                arrayOf("nome", "telefone", "candidato", "problemas", "data", "localizacao"),
                null,
                null,
                null,
                null,
                "id ASC"
            ).use { cursor ->
                val name = cursor.getColumnIndexOrThrow("nome")
                val phone = cursor.getColumnIndexOrThrow("telefone")
                val candidate = cursor.getColumnIndexOrThrow("candidato")
                val problems = cursor.getColumnIndexOrThrow("problemas")
                val date = cursor.getColumnIndexOrThrow("data")
                val location = cursor.getColumnIndexOrThrow("localizacao")
                return buildList {
                    while (cursor.moveToNext()) {
                        val problemArray = JSONArray(cursor.getString(problems))
                        add(
                            SurveyResponse(
                                cursor.getString(name),
                                cursor.getString(phone),
                                cursor.getString(candidate),
                                (0 until problemArray.length()).map(problemArray::getString),
                                cursor.getString(date),
                                cursor.getString(location)
                            )
                        )
                    }
                }
            }
        }
    }

    fun clear(context: Context) {
        Database(context).use { helper ->
            helper.writableDatabase.delete(TABLE_RESPONSES, null, null)
        }
    }
}
