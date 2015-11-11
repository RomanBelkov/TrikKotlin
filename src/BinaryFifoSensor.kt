import rx.Subscriber
import rx.subjects.PublishSubject
import java.io.Closeable
import java.io.FileInputStream
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

/**
 * Created by Roman Belkov on 25.09.15.
 */

abstract class BinaryFifoSensor<T>(val path: String, val dataSize: Int, val bufSize: Int, timeout: Int): Closeable, AutoCloseable {

    constructor(path: String, dataSize: Int, bufSize: Int) : this(path, dataSize, bufSize, -1)

    private var subject   = PublishSubject.create<T>()
    private val bytes     = ByteArray(bufSize)
    private var isStarted = false
    private var isClosing = false
    private var offset    = 0
    private var threadHandler : Thread? = null

    private fun loop() {
        val inputStream = FileInputStream(path)

        tailrec fun reading (inputStream: FileInputStream) {
            if (isClosing == true) {inputStream.close(); return}

            val readCount = inputStream.read(bytes, 0, bytes.size)
            val blocks    = readCount / dataSize
            offset = 0
            for (i in 1..blocks) {
                parse(bytes, offset).ifPresent { subject.onNext(it) }
                offset += dataSize
            }
            reading(inputStream)
        }

        threadHandler = thread { reading(inputStream) }
    }

    abstract fun parse(bytes: ByteArray, offset: Int): Optional<T>

    open fun start() {
        if (isStarted == true) throw Exception("Calling start() second time is prohibited")
        loop()
        isStarted = true
    }

    fun read(): T? {
        if (isStarted == false) {
            println("TrikKotlin: Starting the sensor for you!")
            start()
        }

        val countdown = CountDownLatch(1)
        var result: T? = null
        thread {
            subject.asObservable().subscribe(object : Subscriber<T>() {
                override fun onNext(p0: T) {
                    result = p0
                    this.unsubscribe()
                    countdown.countDown()
                }

                override fun onCompleted() = Unit

                override fun onError(p0: Throwable) = throw p0

            })
        }

        countdown.await()
        return result
    }

    fun stop() {
        if (isStarted == false) throw Exception("Calling stop() before start() is prohibited")
        close()
        subject = PublishSubject.create<T>()
    }

    fun toObservable() = subject.asObservable()

    override fun close() {
        if (isStarted == true) {
            isClosing = true
            while (threadHandler!!.isAlive) {
            }
            subject.onCompleted()
            isStarted = false
            isClosing = false
        }
    }
}
