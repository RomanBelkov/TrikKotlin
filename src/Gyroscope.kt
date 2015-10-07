/**
 * Created by Roman Belkov on 07.10.15.
 */
/*-32767, 32767, "/dev/input/by-path/platform-spi_davinci.1-event"*/

class Gyroscope(min: Int = -32767, max: Int = 32767, devicePath: String = "/dev/input/by-path/platform-spi_davinci.1-event") : Sensor3D(min, max, devicePath)