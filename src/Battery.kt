/**
* Created by Roman Belkov on 14.09.15.
*/
public object Battery: PollingSensor() {
    override fun Read(): Int = I2cTrik.ReadWord(0x26)

    fun ReadVoltage(): Double {
        return this.Read() / 13560.577 // 1023.0 * 3.3 * (7.15 + 2.37) / 2.37
    }
}