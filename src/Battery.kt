/**
* Created by Roman Belkov on 14.09.15.
*/

/**
 * This class represents a battery connected to TRIK
 */
public class Battery: PollingSensor() {

    /**
     * This method lets you read raw data from battery
     *
     * @return raw value from i2c bus
     */
    override fun read(): Int = I2cTrik.readWord(0x26)

    /**
     * This method lets you measure battery's voltage
     *
     * @return voltage on battery
     */
    fun readVoltage(): Double {
        return (this.read().toDouble() / 1023.0) * 13.255696203   // 3.3 * (7.15 + 2.37) / 2.37
    }
}