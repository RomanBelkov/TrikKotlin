/**
 * Created by Roman Belkov on 20.10.15.
 */

internal fun intFromTwoBytes(firstByte: Byte, secondByte: Byte): Int = JavaHelpers.GetIntFromTwoBytes(firstByte, secondByte)

internal fun parseInt(s: String) = Integer.parseInt(s)