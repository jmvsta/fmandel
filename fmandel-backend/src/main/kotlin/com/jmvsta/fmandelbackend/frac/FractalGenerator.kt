package com.jmvsta.fmandelbackend.frac

abstract class FractalGenerator {

    /**
     * Обновляет текущий диапазон, который должен быть центрирован по указанным координатам
     * и увеличен или уменьшен на указанный коэффициент масштабирования.
     */
    fun recenterAndZoomRange(range: Rectangle2D, centerX: Double, centerY: Double, scale: Double) {
        val newWidth: Double = range.width * scale
        val newHeight: Double = range.height * scale
        range.x = centerX - newWidth / 2
        range.y = centerY - newHeight / 2
        range.width = newWidth
        range.height = newHeight
    }

    /**
     * Учитывая координаты *x* + *iy* в комплексной плоскости,
     * вычисляет и возвращает количество итераций, прежде чем фрактальная
     * функция покинет ограничивающую область для этой точки.
     */
    abstract fun numIterations(x: Double, y: Double): Int

    companion object {
        /**
         * Эта статическая вспомогательная функция принимает целочисленную координату и преобразует ее
         * в значение двойной точности, соответствующее определенному диапазону.
         * Он используется для преобразования координат пикселей в
         * значения двойной точности для вычисления фракталов и т.д.
         *
         * @param rangeMin - минимальное значение
         * @param rangeMax - максимальнео значение диапазона
         * @param size     размер измерения, из которого берется координата пикселя.
         * Например, это может быть ширина изображения или высота изображения.
         * @param coord    - это координата.
         * Координата должна находиться в диапазоне [0, size].
         */
        fun getCoord(rangeMin: Double, rangeMax: Double, size: Int, coord: Int): Double {
            val range = rangeMax - rangeMin
            return rangeMin + range * coord.toDouble() / size.toDouble()
        }
    }
}
