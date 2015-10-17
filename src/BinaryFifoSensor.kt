import rx.Subscriber
import rx.subjects.PublishSubject
import java.io.Closeable
import java.io.FileInputStream
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread

/**
 * Created by Roman Belkov on 25.09.15.
 */

abstract class BinaryFifoSensor<T>(val path: String, val dataSize: Int, val bufSize: Int, timeout: Int): Closeable, AutoCloseable {

    constructor(path: String, dataSize: Int, bufSize: Int) : this(path, dataSize, bufSize, -1)

    private val subject = PublishSubject.create<T>()
    private val bytes    = ByteArray(bufSize)
    private var isStarted = false
    private var isClosing = false
    private var offset = 0

    private fun loop(): Thread {
        val inputStream = FileInputStream(path)

        tailrec fun reading (inputStream: FileInputStream) {

            val readCount = inputStream.read(bytes, 0, bytes.size())
            val blocks    = readCount / dataSize
            offset = 0
            for (i in 1..blocks) {
                Parse(bytes, offset).ifPresent { subject.onNext(it) }
                offset += dataSize
            }

            reading(inputStream)
        }

        val threadHandler = thread { if (isClosing == false) reading(inputStream) }

        return threadHandler
    }

    abstract fun Parse(bytes: ByteArray, offset: Int): Optional<T>

    open fun Start() {
        if (isStarted == true) throw Exception("Calling Start() second time is prohibited")
        loop()
        isStarted = true
    }

    fun Read(): T? {
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

    fun Stop() {
        subject.onCompleted()
        isStarted = false
    }

    fun ToObservable() = subject.asObservable()

    override fun close() {
        isClosing = true
        Stop()
    }
}
