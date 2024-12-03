package com.jmvsta.fmandelbackend.frac

class BurningShip(maxIterations: Int) : FractalGenerator(maxIterations) {

    override fun numIterations(x: Double, y: Double, bailout: Double): CalcResult {
        var zx = 0.0
        var zy = 0.0
        var iteration = 0
        val bailoutSquared = bailout * bailout
        var zx2 = 0.0
        var zy2 = 0.0

        while (iteration < maxIterations) {
            val zxAbs = Math.abs(zx)
            val zyAbs = Math.abs(zy)

            zx2 = zxAbs * zxAbs
            zy2 = zyAbs * zyAbs

            if (zx2 + zy2 > bailoutSquared) break

            val tempZx = zx2 - zy2 + x
            zy = 2 * zxAbs * zyAbs + y
            zx = tempZx
            iteration++
        }

        return CalcResult(zx, zy, zx2, zy2, (if (iteration == maxIterations) -1 else iteration))
    }
}