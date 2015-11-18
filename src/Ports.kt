/**
 * Created by Roman Belkov on 15.07.2015.
 */

public enum class ServoPorts(val path: String) {
    S1("ecap.2/"),
    S2("ecap.1/"),
    S3("ecap.0/"),
    S4("ehrpwm.0:1/"),
    S5("ehrpwm.1:0/"),
    S6("ehrpwm.1:0/");
}

public enum class MotorPorts(val pwmAddress: Int, val powerAddress: Int) {
    M1(0x10, 0x14),
    M2(0x11, 0x15),
    M3(0x13, 0x17),
    M4(0x12, 0x16);
}

public enum class EncoderPorts(val address: Int) {
    E1(0x30),
    E2(0x31),
    E3(0x33),
    E4(0x32);
}

public enum class AnalogPorts(val address: Int) {
    A1(0x25),
    A2(0x24),
    A3(0x23),
    A4(0x22),
    A5(0x21),
    A6(0x20);
}

public enum class VideoSource {
    USB, VP1, VP2;
}

public enum class ButtonEventCode(val code: Int) {
    Sync(0),
    Escape(1),
    Enter(28),
    Power(116),
    Up(103),
    Left(105),
    Right(106),
    Down(108);

    override fun toString(): String = code.toString()
}