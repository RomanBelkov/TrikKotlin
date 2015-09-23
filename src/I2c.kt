/**
 * Created by Roman Belkov on 07.08.2015.
 */

public interface I2c {
    fun Open()

    fun Close()

    fun Write(address: Byte, byte: Byte)

    fun Write(address: Byte, value: Short)

    fun ReadWord(address: Byte): Int

    fun ReadAllBytes(address: Byte): Int
}