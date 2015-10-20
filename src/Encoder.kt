/**
* Created by Roman Belkov on 14.09.15.
*/
public class Encoder(val I2cAddress : Int) : PollingSensor() {

    constructor(encoderPort: EncoderPorts) : this(encoderPort.I2cAddress())

    override fun read(): Long = I2cTrik.readAllBytes(I2cAddress)

    fun reset() = I2cTrik.writeWord(I2cAddress, 0)
}