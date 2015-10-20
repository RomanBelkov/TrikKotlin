/**
 * Created by Roman Belkov on 06.10.15.
 */
final class JavaHelpers {
    public static int GetIntFromTwoBytes(byte firstByte, byte secondByte) {
        return firstByte << 8  | secondByte;
    }
}
