package com.jmvsta.fmandelbackend.frac

class Mandelbrot : FractalGenerator() {

    /**
     * Этот метод реализует итерационную функцию для фрактала Мандельброта.
     * Требуется два числа double для действительной и мнимой частей комплекса
     * plane и возвращается количество итераций для соответствующей
     * координаты.
     */
    override fun numIterations(x: Double, y: Double): Int {
        /** Start with iterations at 0.  */
        var iteration = 0
        /** Initialize zreal and zimaginary.  */
        var zreal = 0.0
        var zimaginary = 0.0
        /**
         * Вычисляем Zn = Zn-1 ^ 2 + c, где значения представляют собой комплексные числа, представленные
         * по zreal и zimaginary, Z0 = 0, а c - особая точка в
         * фрактал, который мы показываем (заданный x и y). Это повторяется
         * до Z ^ 2> 4 (абсолютное значение Z больше 2) или максимум
         * достигнуто количество итераций.
         */
        while (iteration < MAX_ITERATIONS &&
            zreal * zreal + zimaginary * zimaginary < 4
        ) {
            val zrealUpdated = zreal * zreal - zimaginary * zimaginary + x
            val zimaginaryUpdated = 2 * zreal * zimaginary + y
            zreal = zrealUpdated
            zimaginary = zimaginaryUpdated
            iteration += 1
        }
        /**
         * Если количество максимальных итераций достигнуто, возвращаем -1, чтобы
         * указать, что точка не вышла за границу.
         */
        return if (iteration == MAX_ITERATIONS) -1 else iteration
    }

    /**
     * Реализация toString() в этой реализации фрактала. Возвращает
     * название фрактала: «Мандельброт».
     */
    override fun toString(): String {
        return "Mandelbrot"
    }

    companion object {
        /**
         * Константа для количества максимальных итераций.
         */
        const val MAX_ITERATIONS = 2500
    }
}
