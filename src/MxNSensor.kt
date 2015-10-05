import java.io.BufferedOutputStream
import java.io.Closeable
import java.io.FileOutputStream
import java.util.*

/**
 * Created by Roman Belkov on 02.10.15.
 */
class MxNSensor(val scriptPath: String,val commandPath: String, sensorPath: String): StringFifoSensor<Array<Int>>(sensorPath), AutoCloseable {

    constructor(videoSource: VideoSource) : this(videoSource.SensorPath(), "/run/mxn-sensor.in.fifo", "/run/mxn-sensor.out.fifo")

    private fun script(command: String) = Shell.Send("$scriptPath $command")

    var stream: FileOutputStream? = null
    //var commandFifo: BufferedOutputStream? = null //not needed
    var isCancelled = false
    var gridSize = Pair(3, 3)
        get() = field
        set(p) {
            field = p
            if (stream == null) throw Exception("Missing Start() call before changing grid size")
            stream!!.write("mxn ${p.first} ${p.second}".toByteArray())
        }

    override fun Start() {
        script("start")
        super.Start()

        stream = FileOutputStream(commandPath)
        //commandFifo = BufferedOutputStream(stream)

    }

    override fun Stop() {   //revise .NET code of this method (forgetting to close stream?)
        super.Stop()
        stream!!.close()
        //commandFifo?.close()
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
        //commandFifo?.close()
        stream?.close()
        //super.close()

    }
}