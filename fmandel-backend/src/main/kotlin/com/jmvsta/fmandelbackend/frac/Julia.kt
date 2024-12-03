package com.jmvsta.fmandelbackend.frac

class Julia(maxIterations: Int): FractalGenerator(maxIterations) {

    override fun numIterations(x: Double, y: Double, bailout: Double): CalcResult {
        var zx = x
        var zy = y
        var iteration = 0
        var zx2 = 0.0
        var zy2 = 0.0
        val cReal = -0.7
        val cImaginary = 0.27015
        while (iteration < maxIterations && zx2 + zy2 <= maxIterations) {
            zx2 = zx * zx
            zy2 = zy * zy

            val tempZx = zx2 - zy2 + cReal
            zy = 2 * zx * zy + cImaginary
            zx = tempZx
            iteration++
        }
        return CalcResult(zx, zy, zx2, zy2, (if (iteration == maxIterations) -1 else iteration))
    }
}