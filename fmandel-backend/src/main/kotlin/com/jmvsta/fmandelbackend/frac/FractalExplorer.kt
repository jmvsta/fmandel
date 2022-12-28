package com.jmvsta.fmandelbackend.frac

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import kotlin.system.measureTimeMillis

class FractalExplorer(
    /**
     * Целочисленный размер отображения - это ширина и высота отображения в пикселях.
     */
    private val width: Int,
    private val height: Int
) {
    /**
     * Ссылка JImageDisplay для обновления отображения с помощью различных методов как
     * фрактал вычислен.
     */
    private val display: BufferedImage

    /**
     * Объект FractalGenerator для каждого типа фрактала.
     */
    private val fractal: FractalGenerator

    /**
     * Объект Rectangle2D.Double, который определяет диапазон
     * то, что мы в настоящее время показываем.
     */
    private var range: Rectangle2D

    /**
     * Конструктор, который принимает размер отображения, сохраняет его и
     * инициализирует объекты диапазона и фрактал-генератора.
     */
    init {
        /** Размер дисплея   */
        /** Инициализирует фрактальный генератор и объекты диапазона.  */
        fractal = Mandelbrot()
        range = Rectangle2D(width = 4.0 * width / height)
        display = BufferedImage(width, height, TYPE_INT_ARGB)
    }

    /**
     * Приватный вспомогательный метод для отображения фрактала. Этот метод проходит
     * через каждый пиксель на дисплее и вычисляет количество
     * итераций для соответствующих координат во фрактале
     * Область отображения. Если количество итераций равно -1, установит цвет пикселя.
     * в черный. В противном случае выберет значение в зависимости от количества итераций.
     * Обновит дисплей цветом для каждого пикселя и перекрасит
     * JImageDisplay, когда все пиксели нарисованы.
     */
    fun drawFractal(): BufferedImage {

//        for (i in 0 until width) {
//            for (j in 0 until height) {
//                drawPixel(i, j)
//            }
//        }
        val time = measureTimeMillis {
//            val part = width/12

            runBlocking {
                (0 until  width).map { i ->
                    CoroutineScope(Dispatchers.Default).async {
                        for (j in 0 until height) {
                            drawPixel(i, j)
                        }
                    }
                }.awaitAll()
            }
        }
        println("Time: $time")
        return display
    }


    fun drawFractal(x: Int, y: Int, direction: String): BufferedImage {
        val xCoordinate = FractalGenerator.getCoord(range.x, range.x + range.width, width, x);
        val yCoordinate = FractalGenerator.getCoord(range.y, range.y + range.height, height, y);

        fractal.recenterAndZoomRange(range, xCoordinate, yCoordinate, if (direction == "UP") 1.5 else 0.5)

        return drawFractal()
    }

    private fun drawPixel(x: Int, y: Int) {
        /**
         * Находим соответствующие координаты xCoord и yCoord
         * в области отображения фрактала.
         */
        val xCoordinate = FractalGenerator.getCoord(range.x, range.x + range.width, width, x)
        val yCoordinate = FractalGenerator.getCoord(range.y, range.y + range.height, height, y)

        /**
         * Вычисляем количество итераций для координат в
         * область отображения фрактала.
         */
        val iteration = fractal.numIterations(xCoordinate, yCoordinate)

        if (iteration == -1) {
            display.setRGB(x, y, java.awt.Color.black.rgb)
        } else {
            /**
             * В противном случае выбераем значение оттенка на основе числа
             * итераций.
             */
//            val hue = 0.6f + iteration.toFloat() / 200f
            val hue = 0.6f + iteration.toFloat() / 2000f
            val rgbColor: Int = java.awt.Color.HSBtoRGB(hue, 15f, 1f)
            /** Обновляем дисплей цветом для каждого пикселя.  */
            display.setRGB(x, y, rgbColor)
        }
    }

    fun reset(): FractalExplorer {
        range = Rectangle2D(width = 4.0 * width / height)
        return this
    }


}
