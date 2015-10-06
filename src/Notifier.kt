import java.io.Closeable
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Created by Roman Belkov on 25.09.15.
 */

class Notifier<T>() {

    val lock = ReentrantLock()
    val observers = ArrayList<Observer<T>>()
    //fun source() =

    fun onCompleted() {
        lock.withLock { observers.forEach { it.onCompleted() } }
        observers.clear()
    }

    fun onError(e: Throwable) {
        lock.withLock { observers.forEach { it.onError(e) } }
        observers.clear()
    }

    fun onNext(t: T) =
        try {
            lock.withLock { observers.forEach { it.onNext(t) } }
        } catch (e: Exception) {
            onError(e)
        }

    fun Publish(): Observable<T> {
        return object : Observable<T> {

            //private var obs: Observer<T>? = null

            override fun Subscribe(observer: Observer<T>): Closeable {
                //obs = observer
                lock.withLock { observers.add(observer) }
                return object : Closeable {
                    override fun close() {
                        lock.withLock { observers.remove(observer) }
                    }

                }
            }
        }
    }
}
