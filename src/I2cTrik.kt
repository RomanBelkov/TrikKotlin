import java.io.IOException

/**
 * Created by Roman Belkov on 07.08.2015.
 */

public object I2cTrik : I2c {

    private val deviceAddress: Byte = 0x48
    private var fileDescriptor = -1
    private val trikBusName = "/dev/i2c-2"

    override fun Open() {
        val fd = I2cNative.openBus(trikBusName, deviceAddress)
        if (fd < 0) {
            throw IOException("Cannot open file handle for $trikBusName got $fd back.")
        } else {
            fileDescriptor = fd
        }
    }

    override fun Close() {
        I2cNative.closeBus(fileDescriptor)
    }

    override fun Write(address: Byte, byte: Byte) {
        val ret = I2cNative.writeByte(fileDescriptor, address, byte)
        if (ret < 0) {
            throw IOException("Error writing to $deviceAddress. Got $ret.")
        }
    }

    override fun Write(address: Byte, value: Short) {
        val ret = I2cNative.writeShort(fileDescriptor, address, value)
        if (ret < 0) {
            throw IOException("Error writing to $address. Got $ret.")
        }
    }

    override fun ReadWord(address: Byte): Int {
        val ret = I2cNative.readWord(fileDescriptor, address)
        if (ret < 0) {
            throw IOException("Error reading from $address. Got $ret.")
        }
        return ret
    }

    override fun ReadAllBytes(address: Byte): Int {
        val ret = I2cNative.readAllBytes(fileDescriptor, address)
        if (ret < 0) {
            throw IOException("Error reading from $address. Got $ret.")
        }
        return ret
    }

}