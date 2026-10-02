package com.example.app_eleitoral

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

object Ui {
    val green = Color.rgb(46, 125, 50)

    fun root(activity: AppCompatActivity): LinearLayout =
        LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 18, 28, 24)
            setBackgroundResource(R.drawable.fundofinal)
            addView(ImageView(activity).apply {
                setImageResource(R.drawable.eleitoral)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                contentDescription = "Logo da pesquisa eleitoral"
            }, LinearLayout.LayoutParams(-1, 92))
        }

    fun title(activity: AppCompatActivity, text: String): TextView =
        TextView(activity).apply {
            this.text = text
            textSize = 25f
            setTextColor(Color.rgb(20, 35, 55))
            gravity = Gravity.CENTER
            setPadding(0, 6, 0, 22)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

    fun button(activity: AppCompatActivity, text: String, action: () -> Unit) =
        Button(activity).apply {
            this.text = text
            setOnClickListener { action() }
            setTextColor(Color.WHITE)
            setBackgroundColor(green)
            minHeight = 52
            setPadding(12, 0, 12, 0)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                56
            ).apply {
                topMargin = 8
                bottomMargin = 8
            }
        }

    fun message(activity: AppCompatActivity, text: String) {
        AlertDialog.Builder(activity).setMessage(text).setPositiveButton("OK", null).show()
    }

    fun space(activity: AppCompatActivity, height: Int = 12) =
        View(activity).apply { layoutParams = LinearLayout.LayoutParams(1, height) }
}
