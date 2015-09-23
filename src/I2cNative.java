/**
 * Created by Roman Belkov on 03.08.2015.
 */

class I2cNative {

    private I2cNative() {
        // we do not allow constructing I2cNative objects
    }

    static {
        System.loadLibrary("I2cWrap");
    }

    /**
     * Opens linux file for reading/writing, returning a file handle.
     *
     * @param busName file name of device. For TRIK i2c should be /dev/i2c-2.
     * @return file descriptor or i2c bus.
     */
    public static native int openBus(String busName, byte deviceAddress);

    /**
     * Closes linux file, associated with i2c bus.
     *
     * @param fd file descriptor
     */
    public static native int closeBus(int fd);

    /**
     * Writes one byte to a specified address inside the i2c bus.
     *
     * @param fd            file descriptor of i2c bus
     * @param address       address in the i2c bus
     * @param value         byte to be written to the device
     * @return result of operation. Zero is OK, everything less than a zero means there was an error.
     */
    public static native int writeByte(int fd, byte address, byte value);

    /**
     * Writes number of bytes to a specified address inside the i2c bus.
     *
     * @param fd            file descriptor of i2c bus
     * @param address       address in the device
     * @param value         two bytes to be written
     * @return result of operation. Zero is OK, everything less than a zero means there was an error.
     */
    public static native int writeShort(int fd, byte address, short value);

    /**
     * Reads one byte from a specified address inside the i2c bus.
     *
     * @param fd            file descriptor of i2c bus
     * @param address       address in the device
     * @return number from 0 to 65535 if reading was successful (2 bytes). Negative number if reading failed.
     */
    public static native int readWord(int fd, byte address);

    /**
     * Reads number of bytes from a specified address inside the i2c bus.
     *
     * @param fd            file descriptor of i2c bus
     * @param address  address in the device
     * @return number that represents 4 bytes read. Negative number if reading failed.
     */
    public static native int readAllBytes(int fd, byte address);
}