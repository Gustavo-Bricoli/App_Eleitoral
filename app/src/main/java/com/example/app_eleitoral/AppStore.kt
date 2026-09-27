package com.example.app_eleitoral

import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONArray

data class SurveyResponse(
    val name: String,
    val phone: String,
    val candidate: String,
    val problems: List<String>,
    val date: String,
    val location: String
)

object SurveyDraft {
    var candidate: String = ""
    var problems: List<String> = emptyList()
    var mode: String = "Espontânea"

    fun clear() {
        candidate = ""
        problems = emptyList()
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
                    name TEXT NOT NULL,
                    phone TEXT NOT NULL,
                    candidate TEXT NOT NULL,
                    problems TEXT NOT NULL,
                    date TEXT NOT NULL,
                    location TEXT NOT NULL
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
                put("name", response.name)
                put("phone", response.phone)
                put("candidate", response.candidate)
                put("problems", JSONArray(response.problems).toString())
                put("date", response.date)
                put("location", response.location)
            }
            helper.writableDatabase.insertOrThrow(TABLE_RESPONSES, null, values)
        }
    }

    fun load(context: Context): List<SurveyResponse> {
        Database(context).use { helper ->
            helper.readableDatabase.query(
                TABLE_RESPONSES,
                arrayOf("name", "phone", "candidate", "problems", "date", "location"),
                null,
                null,
                null,
                null,
                "id ASC"
            ).use { cursor ->
                val name = cursor.getColumnIndexOrThrow("name")
                val phone = cursor.getColumnIndexOrThrow("phone")
                val candidate = cursor.getColumnIndexOrThrow("candidate")
                val problems = cursor.getColumnIndexOrThrow("problems")
                val date = cursor.getColumnIndexOrThrow("date")
                val location = cursor.getColumnIndexOrThrow("location")
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
