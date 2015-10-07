import java.io.Closeable
import java.io.FileInputStream
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by Roman Belkov on 25.09.15.
 */

abstract class BinaryFifoSensor<T>(val path: String, val dataSize: Int, val bufSize: Int, timeout: Int): Closeable, AutoCloseable {

    constructor(path: String, dataSize: Int, bufSize: Int) : this(path, dataSize, bufSize, -1)

    private val notifier = Notifier<T>()
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
                Parse(bytes, offset).ifPresent { notifier.onNext(it) }
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
        return WaitForObservable(notifier.Publish())
    }

    fun Stop() {
        notifier.onCompleted()
        isStarted = false
    }

    override fun close() {
        isClosing = true
        Stop()
    }
}
