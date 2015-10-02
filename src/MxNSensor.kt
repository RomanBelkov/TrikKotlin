import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.util.*

/**
 * Created by Roman Belkov on 02.10.15.
 */
class MxNSensor(scriptPath: String,val commandPath: String, sensorPath: String): StringFifoSensor<Array<Int>>(sensorPath) {

    constructor(videoSource: VideoSource) : this(videoSource.SensorPath(), "/run/mxn-sensor.in.fifo", "/run/mxn-sensor.out.fifo")

    var stream: FileOutputStream? = null
    var commandFifo: BufferedOutputStream? = null
    var isCancelled = false
    var gridSize = Pair(3, 3)
        get() = field
        set(p) {
            field = p
            if (commandFifo == null) throw Exception("Missing Start() call before changing grid size")
            //commandFifo.WriteLine("mxn " + (string m) + " " + (string n))
            //commandFifo.WriteLine("mxn " + (string m) + " " + (string n))
        }

    override fun Start() {
        //script("start")
        super.Start()

        stream = FileOutputStream(commandPath)
        commandFifo = BufferedOutputStream(stream)

    }

    override fun Stop() {   //revise .NET code of this method (forgetting to close stream)
        super.Stop()
        commandFifo!!.close()
        //script("stop")
    }


    override fun Parse(string: String): Optional<Array<Int>> {
        throw UnsupportedOperationException()
    }
}