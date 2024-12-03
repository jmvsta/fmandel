package com.jmvsta.fmandelbackend.frac

abstract class FractalGenerator(val maxIterations: Int) {

    fun recenterAndZoomRange(range: Rectangle2D, centerX: Double, centerY: Double, scale: Double) {
        val newWidth = range.width * scale
        val newHeight = range.height * scale
        range.x = centerX - newWidth / 2
        range.y = centerY - newHeight / 2
        range.width = newWidth
        range.height = newHeight
    }

    fun recenter(range: Rectangle2D, centerX: Double, centerY: Double) {
        range.x = centerX - range.width / 2
        range.y = centerY - range.height / 2
    }


    abstract fun numIterations(x: Double, y: Double, bailout: Double = 2.0): CalcResult

    data class CalcResult(val zx: Double, val zy: Double, val zx2: Double, val zy2: Double, val iterations: Int)

}
