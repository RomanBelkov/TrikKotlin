import rx.Subscriber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread

/**
 * Created by Roman Belkov on 14.10.15.
 */

class ButtonEvent(val button: ButtonEventCode, val isPressed: Boolean) {

    constructor(code: Int, isPressed: Boolean) : this(
        when(code) {
            0   -> ButtonEventCode.Sync
            1   -> ButtonEventCode.Escape
            28  -> ButtonEventCode.Enter
            116 -> ButtonEventCode.Power
            103 -> ButtonEventCode.Up
            105 -> ButtonEventCode.Left
            106 -> ButtonEventCode.Right
            108 -> ButtonEventCode.Down
            else -> throw Exception("Received unknown code")
    }, isPressed)

    fun asPair() = Pair(button, isPressed)

    override fun toString() = "${button.toString()} ${isPressed.toString()}"
}

class Buttons(deviceFilePath: String) : BinaryFifoSensor<ButtonEvent>(deviceFilePath, 16, 1024) {

    constructor() : this("/dev/input/event0")

    var clicksOnly = true

    override fun parse(bytes: ByteArray, offset: Int): Optional<ButtonEvent> {
        if (bytes.size < 16) return Optional.empty()

        val evType  = intFromTwoBytes(bytes[offset + 9], bytes[offset + 8])
        //println("evType:  $evType ")
        val evCode  = intFromTwoBytes(bytes[offset + 11], bytes[offset + 10])
        //println("evCode:  $evCode ")
        val evValue = ByteBuffer.wrap(bytes, offset + 12, 4).order(ByteOrder.LITTLE_ENDIAN).int

        when {
            evType == 1 && (evValue == 1 || !clicksOnly) -> return Optional.of(ButtonEvent(evCode, evValue == 1))
            else                                         -> return Optional.empty()
        }
    }

//    fun checkPressing(button: ButtonEventCode): Boolean {
//        var isPressed = false
//        //val semaphore = Semaphore(0) //TODO to monitor
//
//        thread {
//            this.toObservable().subscribe(object : Subscriber<ButtonEvent>() {
//                override fun onNext(p0: ButtonEvent) {
//                    if (p0.button == button) {
//                        this.unsubscribe()
//                        //semaphore.release()
//                    }
//                }
//
//                override fun onCompleted() = Unit
//
//                override fun onError(p0: Throwable) = throw p0
//
//            })
//        }
//
//        return isPressed
//    }

}