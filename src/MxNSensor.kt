import sun.nio.cs.US_ASCII
import java.io.BufferedOutputStream
import java.io.Closeable
import java.io.FileOutputStream
import java.io.FileWriter
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.util.*

/**
 * Created by Roman Belkov on 02.10.15.
 */
class MxNSensor(val scriptPath: String,val commandPath: String, sensorPath: String): StringFifoSensor<Array<Int>>(sensorPath), Closeable, AutoCloseable {

    constructor(videoSource: VideoSource) : this(videoSource.SensorPath(), "/run/mxn-sensor.in.fifo", "/run/mxn-sensor.out.fifo")

    private fun script(command: String) = Shell.Send("$scriptPath $command")

    var stream: FileWriter? = null
    var isCancelled = false
    var gridSize = Pair(3, 3)
        get() = field
        set(p) {
            if (stream == null) throw Exception("Missing Start() call before changing grid size")
            stream?.write("mxn ${p.first} ${p.second}\n")
            stream?.flush()
            field = p
        }

    override fun Start() {
        stream = FileWriter(commandPath, true)
        script("start")
        super.Start()
    }

    override fun Stop() {   //revise .NET code of this method (forgetting to close stream?)
        super.Stop()
        stream?.close()
        script("stop")
    }

    override fun Parse(text: String): Optional<Array<Int>> {
        var parsedText = text.split(" "). drop(1). filter { it != "" }
        val res = parsedText. map { Integer.parseInt(it) }. toTypedArray()
        return Optional.of(res)
    }

    override fun close() {
        isCancelled = true
        Stop()
        stream?.close()
        //super.close()

    }
}