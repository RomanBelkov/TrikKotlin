import java.io.Closeable

/**
 * Created by Roman Belkov on 06.10.15.
 */

interface Observer<T> {

    fun onCompleted()

    fun onError(e: Throwable)

    fun onNext(t: T)
}

abstract class BasicObserver<T> : Observer<T>, Closeable {
    var stopped = false
    abstract fun Next(value: T): Unit
    abstract fun Error(e: Throwable): Unit
    abstract fun Completed(): Unit

    override fun onNext(t: T) {
        if(!stopped) Next(t)
    }

    override fun onError(e: Throwable) {
        if(!stopped) {
            stopped = true
            Error(e)
        }
    }

    override fun onCompleted() {
        if(!stopped) {
            stopped = true
            Completed()
        }
    }
}
