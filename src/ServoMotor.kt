/**
 * Created by Roman Belkov on 15.07.2015.
 */

import rx.Subscriber
import java.io.Closeable
import java.io.File
import kotlin.io.*

/**
 * Class that represents specific servo motor type
 */
public class ServoType(val min : Int, val max: Int, val zero: Int, val stop: Int, val period: Int)

/**
 * Class that represents servo motor
 *
 * @param servoPort port that servo is connected to
 * @param type type of servo
 */
public class ServoMotor(val servoPort: ServoPorts, val type : ServoType): Closeable, Subscriber<Int>() { //TODO second constructor

    private val pwmPath = "/sys/class/pwm/"

    private fun initWriter(targetPath: String, value: String) = File(pwmPath + servoPort.path + targetPath).writeText(value)
    init {
        initWriter("request", "0")
        initWriter("request", "1")
        initWriter("run", "1")
        initWriter("period_ns", type.period.toString())
    }

    private fun writeDuty(duty: Int) = initWriter("duty_ns", duty.toString())

    /**
     * This method is used to set power on servo
     *
     * @param power power to set on servo
     */
    fun setPower(power: Int) {
        val squashedPower = Helpers.limit(-100, 100, power)
        val range: Int
        when {
            squashedPower < 0 -> range = type.zero - type.min
            squashedPower > 0 -> range = type.max - type.zero
            else              -> range = 0
        }
        val duty = (type.zero + range * squashedPower / 100)
        writeDuty(duty)
    }

    /**
     * This method is used to set servo's power to zero
     *
     * Basically, this is setPower(0)
     */
    fun setZero() {
        writeDuty(type.zero)
    }

    private fun release() {
        writeDuty(type.stop)
    }

    override fun onNext(p0: Int) = setPower(p0)

    override fun onError(p0: Throwable) = release()

    override fun onCompleted() = release()

    override fun close() {
        release()
        initWriter("request", "0")
    }
}