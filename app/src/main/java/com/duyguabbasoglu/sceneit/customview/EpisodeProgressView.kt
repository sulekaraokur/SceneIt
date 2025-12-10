package com.duyguabbasoglu.sceneit.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Custom View showing episode watch progress with Canvas and Paint
 * Displays circular progress indicator with watched/total episodes
 */
class EpisodeProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 12f
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#5E35B1")  // Purple
        style = Paint.Style.STROKE
        strokeWidth = 12f
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 32f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    private val rect = RectF()
    private var watchedEpisodes = 0
    private var totalEpisodes = 0
    private var progressPercentage = 0f

    fun setProgress(watched: Int, total: Int) {
        watchedEpisodes = watched
        totalEpisodes = total
        progressPercentage = if (total > 0) {
            (watched.toFloat() / total.toFloat()) * 100f
        } else {
            0f
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2
        val centerY = height / 2
        val radius = (minOf(width, height) / 2) * 0.85f

        // Set bounds for arc
        rect.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        // Draw background circle
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)

        // Draw progress arc
        if (totalEpisodes > 0) {
            val sweepAngle = (progressPercentage / 100f) * 360f
            canvas.drawArc(rect, -90f, sweepAngle, false, progressPaint)
        }

        // Draw text
        val text = "$watchedEpisodes/$totalEpisodes"
        val yPos = centerY - ((textPaint.descent() + textPaint.ascent()) / 2)
        canvas.drawText(text, centerX, yPos, textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minSize = 100 // Minimum size in pixels
        val width = resolveSize(minSize, widthMeasureSpec)
        val height = resolveSize(minSize, heightMeasureSpec)
        val size = minOf(width, height)
        setMeasuredDimension(size, size)
    }
}
