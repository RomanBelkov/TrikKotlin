import java.io.File

/**
 * Created by Roman Belkov on 06.07.2015.
 */


fun main(args: Array<String>) {
    println("Hello, world!")
    //stop = 0; zero = 1400000; min = 625000; max = 2175000; period = 20000000
    //val servoConfig = ServoType(625000, 2175000, 1400000, 0, 20000000)
    //val servo = ServoMotor(ServoPorts.S3, servoConfig)

    /*    ServoMotor(ServoPorts.S3, servoConfig).use { sr ->
            sr.setPower(80)
            Thread.sleep(2000)
            sr.setPower(-80)
            Thread.sleep(2000)
            *//*sr.setZero()
        Thread.sleep(5000)*//*
    }*/

    /*
        val servo = ServoMotor(ServoPorts.S3, servoConfig)

        servo.setPower(80)
        Thread.sleep(2000)
        servo.setPower(-80)
        Thread.sleep(2000)*/

/*    I2cTrik.Open()
    val analogSensor = AnalogSensor(AnalogPorts.A3)
    println(analogSensor.Read())
    Thread.sleep(2000)
    println(analogSensor.Read())
    I2cTrik.Close()*/

//    val video = MxNSensor(VideoSource.VP2)
//    video.Start()
//    //Thread.sleep(2000)
//    video.gridSize = Pair(1, 1)
//    println(video.gridSize)
//    //video.gridSize = Pair(1, 1)
//    val value = video.Read()
//    Thread.sleep(2000)
//    println(value?.get(1))
//    //File("/run/mxn-sensor.in.fifo").writeText("mxn 1 1")
//    Thread.sleep(2000)
//    println(video.Read()?.size())
//    Thread.sleep(2000)
//    val obs = video.ToObservable()
//    obs.subscribe { println(it.get(1)) }

    /*    val fd = I2cNative.openBus("/dev/i2c-2", 0x48)
        println(I2cNative.readByte(fd, 0x23))
        Thread.sleep(2000)
        println(I2cNative.readByte(fd, 0x23))
        Thread.sleep(2000)
        println(I2cNative.readByte(fd, 0x23))
        Thread.sleep(2000)
        println(I2cNative.closeBus(fd))*/

    /*    val handle = I2cNative.openBus("/dev/i2c-2")
        val arr = byteArrayOf(0x10, 0x00)
        val arr2 = byteArrayOf(0x64)
        val first = I2cNative.writeBytes(handle, 0x48, 0x12, 2, 0, arr)
        val second =  I2cNative.writeBytes(handle, 0x48, 0x16, 1, 0, arr2)
        Thread.sleep(10000)
        val res = I2cNative.closeBus(handle)

        println("h:" + handle + "r" + res)
        println(first + second)*/


    //val lol = I2cTrik

    /*    I2cTrik.Open()
        val arr = byteArrayOf(0x10, 0x00)
        val arr2 = byteArrayOf(0x64)
        I2cTrik.Write(0x12, arr, 2, 0)
        //I2cTrik.Write(0x16, arr2, 1, 0)

        val motor = PowerMotor(0x16)
        motor.SetPower(100)


        Thread.sleep(10000)
        motor.Stop()
        I2cTrik.Close()

        println("done")*/

    //0x14
    /*
        servo.setPower(80)
        Thread.sleep(2000)
        servo.setPower(-80)
        Thread.sleep(2000)
        servo.setZero()
        Thread.sleep(5000)*/

    /*    while (true) {
            var led = Led()

            led.setGreen()
            Thread.sleep(5000)
            led.setOrange()
            Thread.sleep(2000)
            led.powerOff()
            Thread.sleep(2000)
        }*/



    return
}