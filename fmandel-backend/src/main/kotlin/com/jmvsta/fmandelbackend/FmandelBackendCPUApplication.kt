package com.jmvsta.fmandelbackend

import com.jmvsta.fmandelbackend.frac.BurningShip
import com.jmvsta.fmandelbackend.frac.FractalExplorer
import com.jmvsta.fmandelbackend.frac.FractalGenerator
import com.jmvsta.fmandelbackend.frac.Julia
import com.jmvsta.fmandelbackend.frac.Mandelbrot
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.apache.commons.io.IOUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0") {

        var fractalExplorer: FractalExplorer? = null
        var fractal: FractalGenerator
        var type: String?
        var iterations: Int

        routing {
            get("/init") {
//                val xSize = (0.99 * (call.request.queryParameters["xSize"]?.toIntOrNull()?:1000)).toInt()
//                val ySize = (0.97 * (call.request.queryParameters["ySize"]?.toIntOrNull()?:1000)).toInt()

                type = call.request.queryParameters["fractal"]
                iterations = call.request.queryParameters["iterations"]?.toInt()?:2500
                val xSize = 400
                val ySize = 400
                fractal = when(type) {
                    "Julia" -> Julia(iterations)
                    "BurningShip" -> BurningShip(iterations)
                    else -> Mandelbrot(iterations)
                }
                fractalExplorer = FractalExplorer(xSize, ySize, fractal)

                ByteArrayOutputStream().use {
                    ImageIO.write(fractalExplorer?.drawFractal(), "PNG", it)
                    call.respondBytes(
                        IOUtils.toByteArray(ByteArrayInputStream(it.toByteArray())),
                        ContentType.Image.PNG,
                        HttpStatusCode.OK
                    )
                }
            }
            get("/frac") {

                val x = call.request.queryParameters["x"]
                val y = call.request.queryParameters["y"]
                val direction = call.request.queryParameters["direction"]
                val operation = call.request.queryParameters["operation"]
                type = call.request.queryParameters["fractal"]
                iterations = call.request.queryParameters["iterations"]?.toInt()?:2500

                ByteArrayOutputStream().use {
                    ImageIO.write(fractalExplorer?.drawFractal(x!!.toInt(), y!!.toInt(), operation!!, direction), "PNG", it)
                    call.respondBytes(IOUtils.toByteArray(ByteArrayInputStream(it.toByteArray())), ContentType.Image.PNG, HttpStatusCode.OK)
                }
            }
        }
    }.start(wait = true)
}

