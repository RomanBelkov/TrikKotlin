import rx.subjects.PublishSubject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import kotlin.concurrent.thread

/**
 * Created by Roman Belkov on 20.10.15.
 */

open class GamepadEvent private constructor() {
    class Pad(val padId: Int, val coords: Pair<Int, Int>) : GamepadEvent() {
        override fun toString() = "pad $padId ${coords.first} ${coords.second}"
    }
    class PadUp(val padId: Int) : GamepadEvent() {
        override fun toString() = "pad $padId up"
    }
    class Button(val buttonId: Int) : GamepadEvent() {
        override fun toString() = "btn $buttonId down"
    }
    class Wheel(val rollValue: Int) : GamepadEvent() {
        override fun toString() = "wheel $rollValue"
    }
    class Stop : GamepadEvent()
}

class GamepadServer(val port: Int = 4545) {
    private val subject = PublishSubject.create<GamepadEvent>()
    private var isCancelled = false
    //private var threadHandle: Thread? = null

    fun parseRequest(request: String) {
        val parsedRequest = request.split(" "). filter { it != "" }

        when {
            parsedRequest[0] == "pad" && parsedRequest.size() == 4
            -> subject.onNext(GamepadEvent.Pad(parseInt(parsedRequest[1]),
                    Pair(parseInt(parsedRequest[2]), parseInt(parsedRequest[3]))))

            parsedRequest[0] == "wheel" && parsedRequest.size() == 2
                -> subject.onNext(GamepadEvent.Wheel(parseInt(parsedRequest[1])))

            parsedRequest[0] == "pad" && parsedRequest[2] == "up" && parsedRequest.size() == 3
                -> subject.onNext(GamepadEvent.PadUp(parseInt(parsedRequest[1])))

            parsedRequest[0] == "btn" && parsedRequest[2] == "down" && parsedRequest.size() == 3
                -> subject.onNext(GamepadEvent.Button(parseInt(parsedRequest[1])))

            else -> throw Exception("Gamepad server failed to parse request")
        }
    }

    private fun startServer() {

        tailrec fun clientLoop(bufferedReader: BufferedReader) {
            if(isCancelled) return

            parseRequest(bufferedReader.readLine())
            return clientLoop(bufferedReader)
        }

        val serverSocket = ServerSocket(port)
        val clientSocket = serverSocket.accept()
        val inReader     = BufferedReader(InputStreamReader(clientSocket.inputStream))

        thread { clientLoop(inReader) }

    }

    fun start() = startServer()

    fun stop() {
        isCancelled = true
    }

    fun toObservable() = subject.asObservable()
}

