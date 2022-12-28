package com.jmvsta.fmandelbackend

import com.jmvsta.fmandelbackend.frac.FractalExplorer
import org.apache.commons.io.IOUtils
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO


@SpringBootApplication
class FmandelBackendApplication

fun main(args: Array<String>) {
    runApplication<FmandelBackendApplication>(*args)
}

@RestController
class FMController {

    var fractalExplorer: FractalExplorer? = null

    @GetMapping("/init", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun initFractal(@RequestParam xSize: Number, @RequestParam ySize: Number): ByteArray {
        if (fractalExplorer == null) {
            fractalExplorer = FractalExplorer(xSize.toInt(), ySize.toInt())
        } else {
            fractalExplorer?.reset()
        }
        ByteArrayOutputStream().use {
            ImageIO.write(fractalExplorer?.drawFractal(), "PNG", it)
            return IOUtils.toByteArray(ByteArrayInputStream(it.toByteArray()))
        }

    }

    @GetMapping("/frac", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getFractal(@RequestParam x: Number, @RequestParam y: Number, @RequestParam direction: String): ByteArray {
        ByteArrayOutputStream().use {
            ImageIO.write(fractalExplorer?.drawFractal(x.toInt(), y.toInt(), direction), "PNG", it)
            return IOUtils.toByteArray(ByteArrayInputStream(it.toByteArray()))
        }
    }

//    @GetMapping("/reset", produces = [MediaType.IMAGE_JPEG_VALUE])
//    fun resetFractal(@RequestParam(required = false) x: Number?, @RequestParam(required = false) y: Number?,
//                   @RequestParam(required = false) direction: String?): ByteArray {
//        fractalExplorer.reset()
//        ByteArrayOutputStream().use {
//            ImageIO.write(fractalExplorer.drawFractal(), "PNG", it)
//            return IOUtils.toByteArray(ByteArrayInputStream(it.toByteArray()))
//        }
//
//    }

}
