import java.io.BufferedReader
import java.io.FileReader
import java.io.Closeable
import java.util.Optional
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread

/**
 * Created by Roman Belkov on 28.09.15.
 */

fun <T> WaitForObservable(observable: Observable<T>): T? {
    val semaphore = Semaphore(0)
    var result: T? = null
    var x: Closeable? = null
    val threadHandle = thread(start = false) {
        //semaphore.acquire()
        x = observable.Subscribe(object: Observer<T> {
        override fun onCompleted() {
            throw UnsupportedOperationException()
        }

        override fun onError(e: Throwable) {
            throw UnsupportedOperationException()
        }

        override fun onNext(t: T) {
            result = t
            x?.close()
            semaphore.release()
        }
    }) }

    threadHandle.start()
    semaphore.acquire()
    return result
}

abstract class StringFifoSensor<T>(val path: String): Closeable, AutoCloseable {
    private val notifier = Notifier<T>()
    private var isStarted = false
    private var isClosing = false

    private fun loop(): Thread {
        val streamReader = BufferedReader(FileReader(path))

        tailrec fun reading (streamReader: BufferedReader) {
            val line = streamReader.readLine()
            Parse(line).ifPresent { notifier.onNext(it) }
            reading(streamReader)
        }

        val threadHandler = thread { if (isClosing == false) reading(streamReader) }

        return threadHandler
    }

    abstract fun Parse(text: String): Optional<T>

    open fun Start() {
        if (isStarted == true) throw Exception("Calling Start() second time is prohibited")
        //handler = loop()
        loop()
        isStarted = true
    }

    fun Read(): T? {
        if (isStarted == false) throw Exception("Calling Read() before Start() is prohibited")
        return WaitForObservable(notifier.Publish())
    }

    open fun Stop() {
        notifier.onCompleted()
        isStarted = false
    }

    fun ToObservable() = notifier.Publish()

    override fun close() {
        isClosing = true
        Stop()
    }

}
