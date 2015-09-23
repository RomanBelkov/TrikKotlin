/**
 * Created by Roman Belkov on 15.07.2015.
 */

import java.io.File
import kotlin.io.*


public class ServoType(val min : Int, val max: Int, val zero: Int, val stop: Int, val period: Int)

public class ServoMotor(val servoPath: ServoPorts, val type : ServoType): java.io.Closeable {

    private val pwmPath = "/sys/class/pwm/"

    private fun initWriter(targetPath: String, value: String) = File(pwmPath + servoPath.Path() + targetPath).writeText(value)
    init {
        initWriter("request", "0")
        initWriter("request", "1")
        initWriter("run", "1")
        initWriter("period_ns", type.period.toString())
    }

    private fun writeDuty(duty: Int) = initWriter("duty_ns", duty.toString())

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

    fun setZero() {
        writeDuty(type.zero)
    }

    private fun Release() {
        writeDuty(type.stop)
    }

    override fun close() {
        Release()
        initWriter("request", "0")
    }
}