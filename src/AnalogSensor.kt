/**
 * Created by Roman Belkov on 08.09.2015.
 */

public class AnalogSensor(val I2cAddress : Int) : PollingSensor() {

    constructor(analogPort: AnalogPorts) : this(analogPort.I2cAddress())

    override fun read(): Int {
        return  I2cTrik.readWord(I2cAddress)
    }
}