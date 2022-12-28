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
//    var name by useState(props.name)
    div {
        id = "mainDiv"
//        css {
//            maxHeight = 100.pct
//            maxWidth = 100.pct
////            display = Display.flex
//        }
        img {
            id = "fractal"
            src = "http://localhost:8080/init?xSize=${window.innerWidth}&ySize=${window.innerHeight}"
//            src = "http://localhost:8080/init?xSize=${this.width}&ySize=${this.height}"
            onWheel = { event ->
                document.getElementById("fractal")?.setAttribute("src",
                    "http://localhost:8080/frac?x=${event.clientX}&y=${event.clientY}&direction=${if(event.deltaY < 0) "DOWN" else "UP"}")
            }
        }
    }
}


