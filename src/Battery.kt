/**
* Created by Roman Belkov on 14.09.15.
*/
public class Battery: PollingSensor() {
    override fun read(): Int = I2cTrik.readWord(0x26)

    fun readVoltage(): Double {
        return (this.read().toDouble() / 1023.0) * 13.255696203   // 3.3 * (7.15 + 2.37) / 2.37
    }
}