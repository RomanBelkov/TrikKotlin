/**
 * Created by Roman Belkov on 15.07.2015.
 */

public enum class ServoPorts {
    S1, S2, S3, S4, S5, S6;

    fun Path(): String {
        when (this) {
            S1 -> return "ecap.2/"
            S2 -> return "ecap.1/"
            S3 -> return "ecap.0/"
            S4 -> return "ehrpwm.0:1/"
            S5 -> return "ehrpwm.1:0/"
            S6 -> return "ehrpwm.1:1/"
        }
    }
}

public enum class MotorPorts {
    M1, M2, M3, M4;

    fun I2cPwmAddress(): Byte {
        when (this) {
            M1 -> return 0x10
            M2 -> return 0x11
            M3 -> return 0x13
            M4 -> return 0x12
        }
    }

    fun I2cPowerAddress(): Byte {
        when (this) {
            M1 -> return 0x14
            M2 -> return 0x15
            M3 -> return 0x17
            M4 -> return 0x16
        }
    }
}

public enum class EncoderPorts {
    E1, E2, E3, E4;

    fun I2cAddress(): Byte {
        when (this) {
            E1 -> return 0x30
            E2 -> return 0x31
            E3 -> return 0x33
            E4 -> return 0x32
        }
    }
}

public enum class AnalogPorts {
    A1, A2, A3, A4, A5, A6;

    fun I2cAddress(): Byte {
        when (this) {
            A1 -> return 0x25
            A2 -> return 0x24
            A3 -> return 0x23
            A4 -> return 0x22
            A5 -> return 0x21
            A6 -> return 0x20
        }
    }
}

public enum class VideoSource {
    USB, VP1, VP2;
}

public enum class ButtonEventCode(val code: Int) {
    Sync(0), Escape(1), Enter(28), Power(116), Up(103), Left(105), Right(106), Down(108);

    fun GetCode() =
        when (this) {
            Sync   -> 0
            Escape -> 1
            Enter  -> 28
            Power  -> 116
            Up     -> 103
            Left   -> 105
            Right  -> 106
            Down   -> 108
        }

    override fun toString(): String = GetCode().toString()
}