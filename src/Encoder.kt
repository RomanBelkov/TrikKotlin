/**
* Created by Roman Belkov on 14.09.15.
*/

/**
 * This class represents an encoder connected to a particular i2c address
 *
 * @param i2cAddress i2c address of encoder
 */
public class Encoder(val i2cAddress: Int) : PollingSensor() {

    /**
     * This class represents an encoder connected to a particular encoder port
     *
     * @param encoderPort controller's port with encoder connected to it
     */
    constructor(encoderPort: EncoderPorts) : this(encoderPort.address)

    /**
     * This method lets you to read current encoder's value
     *
     * @return encoder's value
     */
    override fun read(): Long = I2cTrik.readAllBytes(i2cAddress)

    /**
     * This method lets you to set encoder's value to 0
     */
    fun reset() = I2cTrik.writeWord(i2cAddress, 0)
}