package com.duyguabbasoglu.sceneit.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class SnowView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private data class Snowflake(var x: Float, var y: Float, val speed: Float, val radius: Float)

    private val snowflakes = ArrayList<Snowflake>()
    private val paint = Paint()
    private val flakeCount = 100

    init {
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0 && snowflakes.isEmpty()) {
            createSnowflakes(w, h)
        }
    }

    private fun createSnowflakes(w: Int, h: Int) {
        for (i in 0 until flakeCount) {
            snowflakes.add(
                Snowflake(
                    x = Random.nextFloat() * w, // Ekran genişliğinde rastgele bir yer
                    y = Random.nextFloat() * h, // Rastgele yükseklik
                    speed = Random.nextFloat() * 4 + 1, // Hız 2 ile 7 arası
                    radius = Random.nextFloat() * 10 + 2 // Boyut 2 ile 12 arası
                )
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Her bir kar tanesini çiz ve hareket ettir
        for (flake in snowflakes) {
            canvas.drawCircle(flake.x, flake.y, flake.radius, paint)

            // Aşağı hareket
            flake.y += flake.speed

            // Eğer ekranın altına geldiyse, yukarı (ekran dışına) geri gönder
            if (flake.y > height) {
                flake.y = -flake.radius
                flake.x = Random.nextFloat() * width // Rastgele yeni bir x konumu
            }
        }

        // Kendini tekrar çizmesi için tetikle (Animasyon döngüsü)
        invalidate()
    }
}