package com.duyguabbasoglu.sceneit.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class RatingIndicatorView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rating = 0.0f
    private val rect = RectF()

    init {
        textPaint.color = Color.BLACK
        textPaint.textSize = 40f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.isFakeBoldText = true
    }

    fun setRating(value: Double) {
        rating = value.toFloat()
        invalidate() // Redraw view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val radius = (Math.min(width, height) / 2) * 0.9f

        rect.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius)

        paint.color = Color.LTGRAY
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 15f
        canvas.drawCircle(width / 2, height / 2, radius, paint)

        paint.color = getRatingColor(rating)
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND

        val sweepAngle = (rating / 10f) * 360f
        canvas.drawArc(rect, -90f, sweepAngle, false, paint)

        val text = String.format("%.1f", rating)
        val yPos = (height / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)
        canvas.drawText(text, width / 2, yPos, textPaint)
    }

    private fun getRatingColor(rating: Float): Int {
        return when {
            rating >= 7.0 -> Color.parseColor("#4CAF50")
            rating >= 5.0 -> Color.parseColor("#FFC107")
            else -> Color.parseColor("#F44336")
        }
    }
}