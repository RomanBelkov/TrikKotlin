/**
 * Created by Roman Belkov on 13.07.2015.
 */

import java.io.File
import kotlin.io.*

class Led(val deviceFilePath: String) {
    constructor() : this ("/sys/class/leds/")

    val greenFile = File(deviceFilePath + "/led_green/brightness")
    val redFile   = File(deviceFilePath + "/led_red/brightness")

    //var color = 0 //LedColor.Off

    fun setRed() {
        powerOff()
        redFile.writeText("1")
    }

    fun setGreen() {
        powerOff()
        greenFile.writeText("1")
    }

    fun setOrange() {
        redFile.writeText("1")
        greenFile.writeText("1")
    }

    fun powerOff() {
        redFile.writeText("0")
        greenFile.writeText("0")
    }
}