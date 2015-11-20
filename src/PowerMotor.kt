/**
 * Created by Roman Belkov on 06.08.2015.
 */

/**
 * This class lets you to control power motor connected to given i2c address
 *
 * @param i2cPowerAddress i2c address that corresponds to motor
 */
public class PowerMotor(val i2cPowerAddress: Int) {

    /**
     * This class lets you to control power motor connected to given motor port
     *
     * @param motorPort port that motor is connected to
     */
    constructor(motorPort: MotorPorts) : this(motorPort.powerAddress)

    /**
     * This method lets you to set power on motor
     *
     * @param power power to set on motor
     */
    fun setPower(power: Int) = I2cTrik.writeByte(i2cPowerAddress, Helpers.limit(-100, 100, power))

    /**
     * This method lets you to stop a motor
     *
     * Basically, it is setPower(0)
     */
    fun stop() = setPower(0)
}