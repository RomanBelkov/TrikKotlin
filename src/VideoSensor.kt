import java.io.FileWriter

/**
 * Created by Roman Belkov on 10.10.15.
 */


open class VideoSensorOutput private constructor() {

    class ObjectLocation(val x: Int, val y: Int, val mass: Int) : VideoSensorOutput() {
        constructor(x: String, y: String, mass: String) : this(parseInt(x), parseInt(y), parseInt(mass))

        override fun toString() = "loc $x $y $mass"

    }

    class DetectTarget(val hue: Int, val hueTolerance: Int, val saturation: Int, val saturationTolerance: Int, val value: Int, val valueTolerance: Int) : VideoSensorOutput() {
        constructor(hue: String, hueTolerance: String, saturation: String, saturationTolerance: String, value: String, valueTolerance: String) :
        this(parseInt(hue), parseInt(hueTolerance), parseInt(saturation), parseInt(saturationTolerance), parseInt(value), parseInt(valueTolerance))

        override fun toString() = "hsv $hue $hueTolerance $saturation $saturationTolerance $value $valueTolerance\n"
    }

    fun tryGetTarget(): DetectTarget? =
        when(this) {
            is ObjectLocation -> null
            is DetectTarget   -> this
            else              -> throw Exception("Should never occur")
        }

    fun tryGetLocation(): ObjectLocation? =
        when(this) {
            is DetectTarget   -> null
            is ObjectLocation -> this
            else              -> throw Exception("Should never occur")
        }

}

/**
 * This class represents abstract TRIK video sensor
 *
 * @param scriptPath starting script path
 * @param commandPath input sensor fifo
 * @param sensorPath output sensor fifo
 */
abstract class VideoSensor(val scriptPath: String, val commandPath: String, sensorPath: String) : StringFifoSensor<VideoSensorOutput>(sensorPath) {

    private fun script(command: String) = Shell.send("$scriptPath $command")

    private var stream: FileWriter? = null
    private var isCancelled = false

    /**
     * This value is used to control whether we want to display sensor debug info or not
     */
    var videoOut = true
        get() = field
        set(p) = when {
            stream == null -> throw Exception("Missing Start() call before changing grid size")
            field == p     -> Unit
            else           -> { stream?.write("video_out ${if (p == true) 1 else 0}"); stream?.flush(); field = p }
        }

    /**
     * This method is used to start the sensor
     */
    override fun start() {
        script("start")
        super.start()
        stream = FileWriter(commandPath)
    }

    /**
     * This method is used to send detect command to sensor
     */
    fun detect() {
        if (stream == null) throw Exception("Missing Start() call before calling Detect()")
        stream?.write("detect\n")
        stream?.flush()
    }

    /**
     * This method is used to catch detected info and then re-send it to sensor
     */
    fun setDetectTarget(target: VideoSensorOutput.DetectTarget) {
        if (stream == null) throw Exception("Missing Start() call before calling SetDetectTarget()")
        stream?.write(target.toString())
        stream?.flush()
    }

    /**
     * This method is used to stop the sensor
     */
    override fun stop() {
        stream?.close()
        super.stop()
        script ("stop")
    }
}