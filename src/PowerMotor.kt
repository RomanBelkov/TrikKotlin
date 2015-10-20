/**
 * Created by Roman Belkov on 06.08.2015.
 */

public class PowerMotor(val I2cPowerAddress: Int) {

    constructor(motorPort: MotorPorts) : this(motorPort.I2cPowerAddress())

    fun SetPower(power: Int) = I2cTrik.WriteByte(I2cPowerAddress, Helpers.limit(-100, 100, power)) //todo maybe a power: byte would be better?

    fun Stop() = SetPower(0)
}