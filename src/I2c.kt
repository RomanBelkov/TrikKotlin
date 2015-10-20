/**
 * Created by Roman Belkov on 07.08.2015.
 */

public interface I2c {
    fun Open()

    fun Close()

    fun WriteByte(address: Int, byte: Int)

    fun WriteWord(address: Int, word: Int)

    fun ReadWord(address: Int): Int

    fun ReadAllBytes(address: Int): Long
}