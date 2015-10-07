/**
 * Created by Roman Belkov on 06.10.15.
 */
public final class JavaHelpers {
    public static int GetIntFromTwoBytes(byte firstByte, byte secondByte) throws Exception {
        return firstByte << 8  | secondByte;
    }
}
