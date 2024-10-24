package com.jmvsta.fmandelbackend

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
import kotlin.system.measureTimeMillis


object MandelbrotLibrary {
    init {
        System.loadLibrary("MandelbrotLibrary")
    }

    external fun makePicture(width: Int, height: Int,
                                        xCenter: Double, yCenter: Double,
                                        xMin: Double, xMax: Double,
                                        yMin: Double, yMax: Double,
                                        maxIter: Int, zoomSteps: Int): ByteArray
}

private var xSize = 0
private var ySize = 0
private const val x_min = -2.0
private const val x_max = 1.0
private const val y_min = -1.5
private const val y_max = 1.5
private var zoom = 0
private var maxIter = 2500

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        routing {
            get("/init") {
                xSize = 800
                ySize = 800

                val imageData: ByteArray = MandelbrotLibrary.makePicture(
                    xSize, ySize, 0.0, 0.0,
                    x_min, x_max, y_min, y_max,
                    maxIter, ++zoom
                )
                ByteArrayOutputStream().use {
                    call.respondBytes(
                        IOUtils.toByteArray(ByteArrayInputStream(imageData)),
                        ContentType.Image.JPEG,
                        HttpStatusCode.OK
                    )
                }
            }

            get("/frac") {
                val time = measureTimeMillis {
                    val x = call.request.queryParameters["x"]!!.toDouble()
                    val y = call.request.queryParameters["y"]!!.toDouble()
//                    val direction = call.request.queryParameters["direction"]

                    val xCenter = (x_max - x_min) * x / xSize
                    val yCenter =  (y_max - y_min) * y / ySize
                    val imageData: ByteArray = MandelbrotLibrary.makePicture(
                        xSize, ySize, xCenter, yCenter,
                        x_min, x_max, y_min, y_max,
                        maxIter, ++zoom
                    )
                    ByteArrayOutputStream().use {
                        call.respondBytes(
                            IOUtils.toByteArray(ByteArrayInputStream(imageData)),
                            ContentType.Image.JPEG,
                            HttpStatusCode.OK
                        )
                    }
                }
                println("Time: $time")
            }
            static("/static") {
                resources()
            }
        }
    }.start(wait = true)
}

