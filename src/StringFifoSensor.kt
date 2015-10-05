import java.io.BufferedReader
import java.io.FileReader
import java.io.Closeable
import java.util.Optional
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread

/**
 * Created by Roman Belkov on 28.09.15.
 */

fun <T> WaitForObservable(observable: ObservableK<T>): T? {
    val semaphore = Semaphore(0)
    var result: T? = null
    var x: Closeable? = null
    val threadHandle = thread(start = false) {
        //semaphore.acquire()
        x = observable.Subscribe(object: Observer<T> {
        override fun onCompleted() {
            throw UnsupportedOperationException()
        }

        override fun onError(e: Throwable?) {
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

abstract class StringFifoSensor<T>(val path: String) {
    val notifier = Notifier<T>()

    private var isStarted = false
    //private val latestValue = Optional<T>.empty()

    private fun loop(): Thread {
        val streamReader = BufferedReader(FileReader(path))

        tailrec fun reading (streamReader: BufferedReader) {
            val line = streamReader.readLine()
            Parse(line).ifPresent { notifier.onNext(it) }
            reading(streamReader)
        }

        val threadHandler = thread { reading(streamReader) }

        return threadHandler //thread should be a property
    }

    abstract fun Parse(text: String): Optional<T>

    open fun Start() {
        if (isStarted == true) throw Exception("Calling Start() second time is prohibited")
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

}
