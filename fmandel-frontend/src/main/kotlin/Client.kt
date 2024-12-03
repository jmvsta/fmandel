import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.w3c.fetch.Response
import react.create
import react.dom.client.createRoot

data class Config(val apiUrl: String, val timeout: Int)

val mainScope = MainScope()

fun main() {
    mainScope.launch {
        try {
            val config = loadConfig()
            val container = document.createElement("div")
            document.body!!.appendChild(container)
            val welcome = MandelbrotFractalView.create { url = config.apiUrl }
            createRoot(container).render(welcome)
        } catch (e: Exception) {
            println("Failed to load config: ${e.message}")
        }
    }

}

suspend fun loadConfig(): Config {
    val response: Response = window.fetch("/config.json").await()
    val json = response.json().await()
    return json.unsafeCast<Config>()
}