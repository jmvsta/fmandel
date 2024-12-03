import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.url.URL
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.useEffect
import react.useState

external interface MandelbrotFractalProps : Props {
    var url: String
}

val MandelbrotFractalView = FC<MandelbrotFractalProps> { props ->
    val (selectedFractal, setSelectedFractal) = useState("Mandelbrot")
    val (iterations, setIterations) = useState(2500)
    val (blobUrl, setBlobUrl) = useState<String>("")

    useEffect({
        updateImage()
    }, dependencies(selectedFractal, iterations))

    fun updateImage (
        operation: String,
        x: Int,
        y: Int,
        direction: String? = null
    ) {
        val params = buildString {
            append("fractal=$selectedFractal")
            append("&iterations=$iterations")
            append("&xSize=${window.innerWidth}")
            append("&ySize=${window.innerHeight}")
            if (operation != null) append("&operation=$operation")
            if (x != null) append("&x=$x")
            if (y != null) append("&y=$y")
            if (direction != null) append("&direction=$direction")
        }

        val url = "${props.url}/frac?$params"

        window.fetch(url)
            .then { response -> response.blob() }
            .then { blob ->
                val newBlobUrl = URL.createObjectURL(blob)
                setBlobUrl(newBlobUrl)


                if (blobUrl.isNotEmpty()) {
                    URL.revokeObjectURL(blobUrl)
                }
            }
            .catch { error -> console.error("Failed to fetch image:", error) }
    }

    div {
        id = "mainDiv"
        img {
            id = "fractal"
            src = blobUrl
            onWheel = { event ->
                val direction = if (event.deltaY < 0) "DOWN" else "UP"
                updateImage(operation = "zoom", x = event.clientX, y = event.clientY, direction = direction)
            }
            onMouseDown = { event ->
                updateImage(operation = "move", x = event.clientX, y = event.clientY)
            }
        }
        select {
            onChange = {
                setSelectedFractal(it.target.value)
            }
            option { value = "Mandelbrot"; +"Mandelbrot" }
            option { value = "Julia"; +"Julia" }
            option { value = "BurningShip"; +"Burning Ship" }
        }
        input {
            type = InputType.number
            value = iterations.toString()
            onChange = {
                val value = it.target.value.toIntOrNull()
                if (value != null && value > 0) setIterations(value)
            }
        }
    }
}


