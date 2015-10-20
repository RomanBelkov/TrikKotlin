/**
 * Created by Roman Belkov on 06.08.2015.
 */

public class PowerMotor(val I2cPowerAddress: Int) {

    constructor(motorPort: MotorPorts) : this(motorPort.I2cPowerAddress())

    fun setPower(power: Int) = I2cTrik.writeByte(I2cPowerAddress, Helpers.limit(-100, 100, power))

    fun stop() = setPower(0)
}