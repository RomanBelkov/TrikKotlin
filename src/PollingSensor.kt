import rx.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by Roman Belkov on 18.08.2015.
 */

abstract class PollingSensor {
    val defaultRate: Long = 50
    abstract fun read() : Number

    fun toObservable(pollingRate: Long, unit: TimeUnit) = Observable.interval(pollingRate, unit).map { read() }

    fun toObservable() = toObservable(defaultRate, TimeUnit.MILLISECONDS)
}