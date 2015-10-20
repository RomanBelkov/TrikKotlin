/**
* Created by Roman Belkov on 14.09.15.
*/
public class Encoder(val I2cAddress : Int) : PollingSensor() {

    constructor(encoderPort: EncoderPorts) : this(encoderPort.I2cAddress())

    override fun Read(): Long = I2cTrik.ReadAllBytes(I2cAddress)

    fun Reset() = I2cTrik.WriteWord(I2cAddress, 0)
}