import kotlin.concurrent.thread

/**
 * Created by Roman Belkov on 03.09.2015.
 */

/**
 * This class lets you interact with linux shell
 */
object Shell {
    /**
     * This method is used to send command to shell and then wait for result
     * @param command command
     * @return exit value from command
     */
    fun send(command: String): Int {
        val p = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", command))
        p.waitFor()
        return p.exitValue()
    }

    /**
     * This method lets you send command to shell and then continue without waiting for result.
     * Returns null if command has not finished yet
     *
     * @param command command
     * @return exit value from command (null if command has not finished yet)
     */
    fun post(command: String): Int? {
        var res: Int? = null
        thread { res = send(command) }
        return res
    }
}

public object Helpers {

    /**
     * This method lets you to capture screenshots from controller's screen
     *
     * @param name desired path & name for screenshot
     * @return exit value of fbgrab util
     */
    public fun takeScreenshot(name: String) = Shell.send("fbgrab -S -z 0 $name 2> /dev/null")

    /**
     * This method lets you to squash given value between two borders
     *
     * @param minValue left border
     * @param maxValue right border
     * @param value    value to squash
     * @return squashed value
     */
    public fun <T: Comparable<T>> limit(minValue: T, maxValue: T, value: T): T {
        when {
            value > maxValue -> return maxValue
            value < minValue -> return minValue
            else             -> return value
        }
    }
}