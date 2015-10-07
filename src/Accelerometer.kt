/**
 * Created by Roman Belkov on 07.10.15.
 */
class Accelerometer(min: Int = -32767, max: Int = 32767, devicePath: String = "/dev/input/event1") : Sensor3D(min, max, devicePath)