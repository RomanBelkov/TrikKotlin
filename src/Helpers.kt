import kotlin.concurrent.thread

/**
 * Created by Roman Belkov on 03.09.2015.
 */

object Shell {
    fun Send(command: String): Int {
        val p = Runtime.getRuntime().exec(command)
        p.waitFor()
        return p.exitValue()
    }

    fun Post(command: String): Int {
        var res: Int = 0
        thread { res = Send(command) }
        return res
    }
}

public object Helpers {
    public fun limit<T: Comparable<T>>(minValue: T, maxValue: T, value: T): T {
        when {
            value > maxValue -> return maxValue
            value < minValue -> return minValue
            else             -> return value
        }
    }
}