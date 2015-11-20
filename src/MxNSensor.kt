import java.io.Closeable
import java.io.FileWriter
import java.util.*

/**
 * Created by Roman Belkov on 02.10.15.
 */

/**
 * This class represents MxN TRIK video sensor
 *
 * @param scriptPath starting script path
 * @param commandPath input sensor fifo
 * @param sensorPath output sensor fifo
 */
class MxNSensor(val scriptPath: String,val commandPath: String, sensorPath: String): StringFifoSensor<Array<Int>>(sensorPath), Closeable, AutoCloseable {

    /**
     * This class represents MxN TRIK video sensor
     *
     * @param videoSource source of video (USB cam or video module)
     */
    constructor(videoSource: VideoSource) : this(
        when(videoSource) {
            VideoSource.USB  -> "/etc/init.d/mxn-sensor-webcam.sh"
            else             -> "/etc/init.d/mxn-sensor-ov7670"
        },
        "/run/mxn-sensor.in.fifo", "/run/mxn-sensor.out.fifo")

    private fun script(command: String) = Shell.send("$scriptPath $command")

    private var stream: FileWriter? = null
    private var isCancelled = false

    /**
     * This value is used to set the amount of areas for sensor
     */
    var gridSize = Pair(3, 3)
        get() = field
        set(p) {
            if (stream == null) throw Exception("Missing Start() call before changing grid size")
            stream?.write("mxn ${p.first} ${p.second}\n")
            stream?.flush()
            field = p
        }

    /**
     * This method is used to start the sensor
     */
    override fun start() {
        stream = FileWriter(commandPath, true)
        script("start")
        super.start()
    }

    /**
     * This method is used to stop the sensor
     */
    override fun stop() {   //revise .NET code of this method (forgetting to close stream?)
        super.stop()
        stream?.close()
        script("stop")
    }

    override fun parse(text: String): Optional<Array<Int>> {
        val parsedText = text.split(" "). drop(1). filter { it != "" }
        val res = parsedText. map { Integer.parseInt(it) }. toTypedArray()
        return Optional.of(res)
    }

    /**
     * This method is used to close the sensor
     *
     * NB: we assume we will not use sensor anymore and freeing all resources
     */
    override fun close() {
        isCancelled = true
        stop()
        stream?.close()
    }
}