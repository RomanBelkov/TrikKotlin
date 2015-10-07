/**
 * Created by Roman Belkov on 07.10.15.
 */

class Accelerometer(min: Int = -8192, max: Int = 8192, devicePath: String = "/dev/input/event1") : Sensor3D(min, max, devicePath)