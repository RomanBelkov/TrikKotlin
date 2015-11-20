/**
 * Created by Roman Belkov on 08.09.2015.
 */


/**
 * This class represents an analog sensor connected to a specified i2c port
 *
 * @param i2cAddress i2c address that corresponds to sensor
 */
public class AnalogSensor(val i2cAddress: Int) : PollingSensor() {

    /**
     * This class represents an analog sensor connected to a specified analog port
     *
     * @param analogPort port that sensor is connected to
     */
    constructor(analogPort: AnalogPorts) : this(analogPort.address)

    /**
     * This method lets you to read value from sensor
     *
     * @return value from sensor
     */
    override fun read(): Int = I2cTrik.readWord(i2cAddress)
}