/**
 * Created by Roman Belkov on 13.07.2015.
 */

import java.io.File
import kotlin.io.*

/**
 * This class is used to control on-board three-color LED
 *
 * @param deviceFilePath path to controlling files
 */
class Led(val deviceFilePath: String) {
    /**
     * This class is used to control on-board three-color LED
     */
    constructor() : this ("/sys/class/leds/")

    val greenFile = File(deviceFilePath + "/led_green/brightness")
    val redFile   = File(deviceFilePath + "/led_red/brightness")

    /**
     * LED becomes red
     */
    fun setRed() {
        powerOff()
        redFile.writeText("1")
    }

    /**
     * LED becomes green
     */
    fun setGreen() {
        powerOff()
        greenFile.writeText("1")
    }

    /**
     * LED becomes orange
     */
    fun setOrange() {
        redFile.writeText("1")
        greenFile.writeText("1")
    }

    /**
     * LED becomes switched off
     */
    fun powerOff() {
        redFile.writeText("0")
        greenFile.writeText("0")
    }
}