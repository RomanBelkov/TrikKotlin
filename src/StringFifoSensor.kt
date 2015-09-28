import java.io.BufferedReader
import java.util.concurrent.Future
import kotlin.concurrent.thread
import java.util.Optional
import co.paralleluniverse.*

/**
 * Created by Roman Belkov on 28.09.15.
 */

//data class



//fun <T> Optional<T>.iter(action: (T) -> Unit) =

abstract class StringFifoSensor<T>(path: String) {
    val notifier = Notifier<T>()

    fun loop() {
        fun reading (streamReader: BufferedReader) {
            val line = streamReader.readLine()
            Parse(line).ifPresent { notifier.onNext(it) }
            reading(streamReader)
        }
    }

    abstract fun Parse(string: String): Optional<T>
}