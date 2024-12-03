package com.jmvsta.fmandelbackend.frac

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin

class FractalExplorer(
    private val width: Int,
    private val height: Int,
    private val fractal: FractalGenerator
) {

    private val display: BufferedImage

    private var range: Rectangle2D = Rectangle2D(width = 4.0 * width / height)

    init {
        display = BufferedImage(width, height, TYPE_INT_ARGB)
    }

    fun drawFractal(): BufferedImage {
        runBlocking {
            (0 until width).map { i ->
                CoroutineScope(Dispatchers.Default).async {
                    for (j in 0 until height) {
                        drawPixel(i, j)
                    }
                }
            }.awaitAll()
        }

        return display
    }


    fun drawFractal(x: Int, y: Int, operation: String, direction: String?): BufferedImage {
        val xCoordinate = getCoord(range.x, range.x + range.width, width, x)
        val yCoordinate = getCoord(range.y, range.y + range.height, height, y)
        when (operation) {
            "move" -> fractal.recenter(range, xCoordinate, yCoordinate)
            "zoom" -> fractal.recenterAndZoomRange(range, xCoordinate, yCoordinate, if (direction == "UP") 1.5 else 0.5)
        }
        return drawFractal()
    }

    private fun getCoord(rangeMin: Double, rangeMax: Double, size: Int, coord: Int): Double {
        val range = rangeMax - rangeMin
        return rangeMin + range * coord.toDouble() / size.toDouble()
    }

    private fun drawPixel(x: Int, y: Int) {
        val xCoordinate = getCoord(range.x, range.x + range.width, width, x)
        val yCoordinate = getCoord(range.y, range.y + range.height, height, y)
        val iterRes = fractal.numIterations(xCoordinate, yCoordinate)

        if (iterRes.iterations == -1) {
            display.setRGB(x, y, java.awt.Color.black.rgb)
        } else {
            val smoothIteration = iterRes.iterations + 1 - ln(ln(iterRes.zx2 + iterRes.zy2)) / ln(2.0);
            val hue = 0.6 + smoothIteration / 2500.0
            val saturation = 0.5 + 0.5 * sin(iterRes.iterations.toDouble() / 10)
            val brightness = 0.7 + 0.3 * cos(iterRes.iterations.toDouble() / 15)
            val rgbColor = hsbToRgb(hue % 1.0, 1.0, 1.0)
//            val hue = 0.6f + iterRes.iterations.toFloat() / 2000f
//            val rgbColor: Int = java.awt.Color.HSBtoRGB(hue, 15f, 1f)
            display.setRGB(x, y, rgbColor)
        }
    }

    private fun hsbToRgb(hue: Double, saturation: Double, brightness: Double): Int {
        val h = (hue % 1.0) * 6.0 // Map hue to [0, 6)
        val c = brightness * saturation
        val x = c * (1 - Math.abs(h % 2 - 1))
        val m = brightness - c

        val (r, g, b) = when (h.toInt()) {
            0 -> Triple(c, x, 0.0)
            1 -> Triple(x, c, 0.0)
            2 -> Triple(0.0, c, x)
            3 -> Triple(0.0, x, c)
            4 -> Triple(x, 0.0, c)
            else -> Triple(c, 0.0, x)
        }

        return java.awt.Color(
            ((r + m) * 255).toInt(),
            ((g + m) * 255).toInt(),
            ((b + m) * 255).toInt()
        ).rgb
    }

    fun reset(): FractalExplorer {
        range = Rectangle2D(width = 4.0 * width / height)
        return this
    }

}
