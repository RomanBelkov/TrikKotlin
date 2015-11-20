import rx.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by Roman Belkov on 18.08.2015.
 */

abstract class PollingSensor {
    private val defaultRate: Long = 50
    abstract fun read() : Number

    /**
     * This method returns sensor output as rx observable
     *
     * @param pollingRate determines how fast should we take sensor's output
     * @param unit time measurment unit for pollingRate
     *
     * @return rx observable
     */
    fun toObservable(pollingRate: Long, unit: TimeUnit) = Observable.interval(pollingRate, unit).map { read() }

    /**
     * This method returns sensor output as rx observable
     *
     * Basically, this is toObservable(50, TimeUnit.MILLISECONDS)
     *
     * @return rx observable that produces new value each 50 milliseconds
     */
    fun toObservable() = toObservable(defaultRate, TimeUnit.MILLISECONDS)
}