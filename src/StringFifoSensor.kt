import java.io.BufferedReader
import java.io.FileReader
import java.io.Closeable
import java.util.Optional
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread
import rx.Subscriber
import rx.subjects.PublishSubject
import java.util.concurrent.CountDownLatch

/**
 * Created by Roman Belkov on 28.09.15.
 */

abstract class StringFifoSensor<T>(val path: String): Closeable, AutoCloseable {
    private var subject = PublishSubject.create<T>()
    private var isStarted = false
    private var isClosing = false
    private var threadHandler : Thread? = null

    private fun loop() {
        val streamReader = BufferedReader(FileReader(path))

        tailrec fun reading (streamReader: BufferedReader) {
            if (isClosing == true) {streamReader.close(); return}

            val line = streamReader.readLine()
            if (line == null) stop() else {
                parse(line).ifPresent { subject.onNext(it) }

                reading(streamReader)
            }
        }

        threadHandler = thread { reading(streamReader) }
    }

    abstract fun parse(text: String): Optional<T>

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

        thread { subject.asObservable().subscribe(object : Subscriber<T>() {
            override fun onNext(p0: T) {
                result = p0
                this.unsubscribe()
                countdown.countDown()
            }

            override fun onCompleted() = Unit

            override fun onError(p0: Throwable) = throw p0

        }) }

        countdown.await()
        return result
    }

    open fun stop() {
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
