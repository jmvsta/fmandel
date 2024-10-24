import kotlinx.browser.document
import kotlinx.browser.window
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img

external interface MandelbrotFractalProps : Props {
    var name: String
}

val Welcome = FC<MandelbrotFractalProps> { _ ->
    div {
        id = "mainDiv"
        img {
            id = "fractal"
            src = "http://localhost:8080/init?xSize=${window.innerWidth}&ySize=${window.innerHeight}"
            onWheel = { event ->
                document.getElementById("fractal")?.setAttribute("src",
                    "http://localhost:8080/frac?x=${event.clientX}&y=${event.clientY}&direction=${if(event.deltaY<0) "DOWN" else "UP"}")
            }
        }
    }
}


