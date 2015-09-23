/**
 * Created by Roman Belkov on 18.08.2015.
 */

abstract class PollingSensor {
    val defaultRate = 50
    abstract fun Read() : Int
}