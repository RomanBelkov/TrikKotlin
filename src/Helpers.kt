import kotlin.concurrent.thread

/**
 * Created by Roman Belkov on 03.09.2015.
 */

object Shell {
    fun send(command: String): Int {
        val p = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", command))
        p.waitFor()
        return p.exitValue()
    }

    fun post(command: String): Int {
        var res: Int = 0
        thread { res = send(command) }
        return res
    }
}

public object Helpers {

    public fun takeScreenshot(name: String) = Shell.send("fbgrab -S -z 0 $name 2> /dev/null")

    public fun <T: Comparable<T>> limit(minValue: T, maxValue: T, value: T): T {
        when {
            value > maxValue -> return maxValue
            value < minValue -> return minValue
            else             -> return value
        }
    }
}