/**
 * Created by Roman Belkov on 07.08.2015.
 */

public interface I2c {
    fun open()

    fun close()

    fun writeByte(address: Int, byte: Int)

    fun writeWord(address: Int, word: Int)

    fun readWord(address: Int): Int

    fun readAllBytes(address: Int): Long
}