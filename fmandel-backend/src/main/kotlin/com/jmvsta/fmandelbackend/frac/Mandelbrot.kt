package com.jmvsta.fmandelbackend.frac

class Mandelbrot(maxIterations: Int) : FractalGenerator(maxIterations) {

    override fun numIterations(x: Double, y: Double, bailout: Double): CalcResult {
        var zx = 0.0
        var zy = 0.0
        var iteration = 0
        var zx2 = 0.0;
        var zy2 = 0.0;
        while (iteration < maxIterations) {
            zx2 = zx * zx
            zy2 = zy * zy
            if (zx2 + zy2 > bailout * bailout) break

            zy = 2 * zx * zy + y
            zx = zx2 - zy2 + x
            iteration++
        }
        return CalcResult(zx, zy, zx2, zy2, (if (iteration == maxIterations) -1 else iteration))
    }

    override fun toString(): String {
        return "Mandelbrot"
    }
}
