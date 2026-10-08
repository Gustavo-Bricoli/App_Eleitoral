package com.example.app_eleitoral

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.min

class ChartsActivity : AppCompatActivity() {
    private lateinit var chartContainer: LinearLayout
    private val responses by lazy { AppStore.load(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)
        chartContainer = findViewById(R.id.containerGraficos)
        findViewById<android.widget.Button>(R.id.btnPizza).setOnClickListener { showPie() }
        findViewById<android.widget.Button>(R.id.btnBarras).setOnClickListener { showBars() }
        findViewById<android.widget.Button>(R.id.btnVoltarGraficos).setOnClickListener { finish() }
        showPie()
    }

    private fun showPie() {
        chartContainer.removeAllViews()
        val values = responses.groupingBy { it.candidato.ifBlank { "Não informado" } }.eachCount()
        val chart = PieChartView(this, values)
        chartContainer.addView(chart, LinearLayout.LayoutParams(-1, 300))
        addLegend(values, chart.colors)
    }

    private fun showBars() {
        chartContainer.removeAllViews()
        val values = responses.flatMap { it.problemas }.groupingBy { it }.eachCount()
            .entries.sortedByDescending { it.value }.take(10).associate { it.key to it.value }
        val chart = BarChartView(this, values)
        chartContainer.addView(chart, LinearLayout.LayoutParams(-1, 360))
        addLegend(values, chart.colors)
    }

    private fun addLegend(values: Map<String, Int>, colors: List<Int>) {
        if (values.isEmpty()) {
            chartContainer.addView(TextView(this).apply {
                text = "Ainda não há dados para exibir."
                textSize = 16f
            })
            return
        }
        values.keys.forEachIndexed { index, label ->
            chartContainer.addView(TextView(this).apply {
                text = "■  $label: ${values[label]}"
                textSize = 14f
                setTextColor(colors[index % colors.size])
                setPadding(8, 2, 8, 2)
            })
        }
    }
}

private abstract class ChartView(
    context: android.content.Context,
    protected val values: Map<String, Int>
) : View(context) {
    val colors = listOf(
        Color.rgb(46, 125, 50), Color.rgb(25, 118, 210),
        Color.rgb(239, 108, 0), Color.rgb(123, 31, 162),
        Color.rgb(0, 121, 107), Color.rgb(198, 40, 40),
        Color.rgb(93, 64, 55), Color.rgb(84, 110, 122)
    )
    protected val paint = Paint(Paint.ANTI_ALIAS_FLAG)
}

private class PieChartView(
    context: android.content.Context,
    values: Map<String, Int>
) : ChartView(context, values) {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (values.isEmpty()) return
        val total = values.values.sum().toFloat()
        val diameter = min(width * 0.62f, height * 0.85f)
        val left = (width - diameter) / 2f
        val top = (height - diameter) / 2f
        val bounds = RectF(left, top, left + diameter, top + diameter)
        var start = -90f
        values.values.forEachIndexed { index, value ->
            paint.color = colors[index % colors.size]
            canvas.drawArc(bounds, start, value / total * 360f, true, paint)
            start += value / total * 360f
        }
    }
}

private class BarChartView(
    context: android.content.Context,
    values: Map<String, Int>
) : ChartView(context, values) {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (values.isEmpty()) return
        val max = values.values.maxOrNull()?.coerceAtLeast(1) ?: return
        val barWidth = width.toFloat() / values.size.coerceAtLeast(1) * 0.65f
        val step = width.toFloat() / values.size.coerceAtLeast(1)
        val bottom = height - 30f
        values.values.forEachIndexed { index, value ->
            val left = index * step + (step - barWidth) / 2
            val barHeight = (height - 70f) * value / max
            paint.color = colors[index % colors.size]
            canvas.drawRect(left, bottom - barHeight, left + barWidth, bottom, paint)
            paint.color = Color.DKGRAY
            paint.textSize = 13f
            canvas.drawText(value.toString(), left + barWidth / 2 - 5, bottom - barHeight - 6, paint)
        }
    }
}
