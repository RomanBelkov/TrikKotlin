/**
 * Created by Roman Belkov on 03.09.2015.
 */

public object Helpers {
    public fun limit(minValue: Int, maxValue: Int, value: Int): Int {
        when {
            value > maxValue -> return maxValue
            value < minValue -> return minValue
            else             -> return value
        }
    }
}