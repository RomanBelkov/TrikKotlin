import rx.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by Roman Belkov on 18.08.2015.
 */

abstract class PollingSensor {
    val defaultRate: Long = 50
    abstract fun Read() : Number

    fun ToObservable(pollingRate: Long, unit: TimeUnit) = Observable.interval(pollingRate, unit).map { Read() }

    fun ToObservable() = ToObservable(defaultRate, TimeUnit.MILLISECONDS)
}