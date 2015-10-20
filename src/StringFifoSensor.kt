import java.io.BufferedReader
import java.io.FileReader
import java.io.Closeable
import java.util.Optional
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread
import rx.Subscriber
import rx.subjects.PublishSubject

/**
 * Created by Roman Belkov on 28.09.15.
 */

abstract class StringFifoSensor<T>(val path: String): Closeable, AutoCloseable {
    private val subject = PublishSubject.create<T>()
    private var isStarted = false
    private var isClosing = false

    private fun loop(): Thread {
        val streamReader = BufferedReader(FileReader(path))

        tailrec fun reading (streamReader: BufferedReader) {
            val line = streamReader.readLine()
            if (line == null) stop() else {
                parse(line).ifPresent { subject.onNext(it) }

                reading(streamReader)
            }
        }

        val threadHandler = thread { while(!isClosing) reading(streamReader); streamReader.close() }

        return threadHandler
    }

    abstract fun parse(text: String): Optional<T>

    open fun start() {
        if (isStarted == true) throw Exception("Calling Start() second time is prohibited")
        loop()
        isStarted = true
    }

    fun read(): T? {
        if (isStarted == false) throw Exception("Calling Read() before Start() is prohibited")
        val semaphore = Semaphore(0) //TODO to monitor
        var result: T? = null

        thread { subject.asObservable().subscribe(object : Subscriber<T>() {
            override fun onNext(p0: T) {
                result = p0
                this.unsubscribe()
                semaphore.release()
            }

            override fun onCompleted() = Unit

            override fun onError(p0: Throwable) = throw p0

        }) }

        semaphore.acquire()
        return result
    }

    open fun stop() {
        isClosing = true
        subject.onCompleted()
        isStarted = false
    }

    fun toObservable() = subject.asObservable()

    override fun close() {
        isClosing = true
        stop()
    }

}
