import kotlin.concurrent.thread

/**
 * Created by Roman Belkov on 03.09.2015.
 */

object Shell {
    fun Send(command: String): Int {
        val p = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", command))
        p.waitFor()
        return p.exitValue()
    }

    fun Post(command: String): Int {
        var res: Int = 0
        thread { res = Send(command) }
        return res
    }
}

internal fun intFromTwoBytes(firstByte: Byte, secondByte: Byte): Int = JavaHelpers.GetIntFromTwoBytes(firstByte, secondByte)

public object Helpers {

    public fun TakeScreenshot(name: String) = Shell.Send("fbgrab -S -z 0 $name 2> /dev/null")

    public fun limit<T: Comparable<T>>(minValue: T, maxValue: T, value: T): T {
        when {
            value > maxValue -> return maxValue
            value < minValue -> return minValue
            else             -> return value
        }
    }
}