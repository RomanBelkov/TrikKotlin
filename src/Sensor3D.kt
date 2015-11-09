import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

/**
 * Created by Roman Belkov on 06.10.15.
 */

data class Point(val x: Int, val y: Int,val z: Int)


open class Sensor3D(val min: Int, val max: Int, devicePath: String): BinaryFifoSensor<Point>(devicePath, 16, 1024) {
    private val evAbs = 3
    private val lastArray = Array(3, {0})

    override fun parse(bytes: ByteArray, offset: Int): Optional<Point> {
        if (bytes.size < 16) return Optional.empty()

        val evType  = intFromTwoBytes(bytes[offset + 9], bytes[offset + 8])
        //println("evType:  $evType ")
        val evCode  = intFromTwoBytes(bytes[offset + 11], bytes[offset + 10])
        //println("evCode:  $evCode ")
        val evValue = ByteBuffer.wrap(bytes, offset + 12, 4).order(ByteOrder.LITTLE_ENDIAN).int
        //println("evValue:  $evValue ")
        if (evType == evAbs && evCode < 3) {
            lastArray[evCode] = Helpers.limit(min, max, evValue)
            return Optional.empty<Point>()
        }

        return Optional.of(Point(lastArray[0], lastArray[1], lastArray[2]))
    }
}