package com.jmvsta.fmandelbackend

import com.jmvsta.fmandelbackend.frac.FractalExplorer
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.apache.commons.io.IOUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {

        var fractalExplorer: FractalExplorer? = null

        routing {
            get("/init") {

                val xSize = call.request.queryParameters["xSize"]
                val ySize = call.request.queryParameters["ySize"]

                fractalExplorer = fractalExplorer?.reset() ?: FractalExplorer(xSize!!.toInt(), ySize!!.toInt())

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

                ByteArrayOutputStream().use {
                    ImageIO.write(fractalExplorer?.drawFractal(x!!.toInt(), y!!.toInt(), direction!!), "PNG", it)
                    call.respondBytes(IOUtils.toByteArray(ByteArrayInputStream(it.toByteArray())), ContentType.Image.PNG, HttpStatusCode.OK)
                }
            }
            static("/static") {
                resources()
            }
        }
    }.start(wait = true)
}

