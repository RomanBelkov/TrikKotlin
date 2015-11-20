import java.io.IOException

/**
 * Created by Roman Belkov on 07.08.2015.
 */

/**
 * This class lets you to interact with i2c bus directly
 */
public object I2cTrik : I2c {

    private const val deviceAddress = 0x48
    private var fileDescriptor = -1
    private const val trikBusName = "/dev/i2c-2"

    /**
     * This method opens a connection with i2c bus
     */
    override fun open() {
        val fd = I2cNative.openBus(trikBusName, deviceAddress)
        if (fd < 0) {
            throw IOException("Cannot open file handle for $trikBusName got $fd back.")
        } else {
            fileDescriptor = fd
        }
    }

    /**
     * This method closes connection with i2c bus
     */
    override fun close() {
        I2cNative.closeBus(fileDescriptor)
    }

    /**
     * This method writes byte to address in i2c bus
     *
     * @param address inner address in i2c bus
     * @param byte byte to write
     */
    override fun writeByte(address: Int, byte: Int) {
        val ret = I2cNative.writeByte(fileDescriptor, address, byte)
        if (ret < 0) throw IOException("Error writing to $deviceAddress. Got $ret.")
    }

    /**
     * This method writes word (2 bytes) to address in i2c bus
     *
     * @param address inner address in i2c bus
     * @param word 2 bytes to write
     */
    override fun writeWord(address: Int, word: Int) {
        val ret = I2cNative.writeWord(fileDescriptor, address, word)
        if (ret < 0) throw IOException("Error writing to $address. Got $ret.")
    }

    /**
     * This method reads word (2 bytes) from address in i2c bus
     *
     * @param address inner address in i2c bus
     * @return value read from i2c bus
     */
    override fun readWord(address: Int): Int {
        val ret = I2cNative.readWord(fileDescriptor, address)
        if (ret < 0) throw IOException("Error reading from $address. Got $ret.")
        return ret
    }

    /**
     * This method reads 4 bytes from address in i2c bus
     *
     * @param address inner address in i2c bus
     * @return value read from i2c bus
     */
    override fun readAllBytes(address: Int): Long {
        val ret = I2cNative.readAllBytes(fileDescriptor, address)
        if (ret < 0) throw IOException("Error reading from $address. Got $ret.")
        return ret
    }

}