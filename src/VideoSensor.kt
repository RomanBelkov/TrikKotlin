import java.io.FileWriter

/**
 * Created by Roman Belkov on 10.10.15.
 */


private fun parseInt(s: String) = Integer.parseInt(s)

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

    fun TryGetTarget(): DetectTarget? =
        when(this) {
            is ObjectLocation -> null
            is DetectTarget   -> this
            else              -> throw Exception("Should never occur")
        }

    fun TryGetLocation(): ObjectLocation? =
        when(this) {
            is DetectTarget   -> null
            is ObjectLocation -> this
            else              -> throw Exception("Should never occur")
        }

}

abstract class VideoSensor<T>(val scriptPath: String, val commandPath: String, sensorPath: String) : StringFifoSensor<VideoSensorOutput>(sensorPath) {

    private fun script(command: String) = Shell.Send("$scriptPath $command")

    private var stream: FileWriter? = null
    private var isCancelled = false

    var videoOut = true
        get() = field
        set(p) = when {
            stream == null -> throw Exception("Missing Start() call before changing grid size")
            field == p     -> Unit
            else           -> { stream?.write("video_out ${if (p == true) 1 else 0}"); stream?.flush(); field = p }
        }

    override fun Start() {
        script("start")
        super.Start()
        stream = FileWriter(commandPath)
    }

    fun Detect() {
        if (stream == null) throw Exception("Missing Start() call before calling Detect()")
        stream?.write("detect\n")
        stream?.flush()
    }

    fun SetDetectTarget(target: VideoSensorOutput.DetectTarget) {
        if (stream == null) throw Exception("Missing Start() call before calling SetDetectTarget()")
        stream?.write(target.toString())
        stream?.flush()
    }

    override fun Stop() {
        stream?.close()
        super.Stop()
        script ("stop")
    }
}