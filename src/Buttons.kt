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

    fun AsPair() = Pair(button, isPressed)

    override fun toString() = "${button.toString()} ${isPressed.toString()}"
}

class Buttons(deviceFilePath: String) : BinaryFifoSensor<ButtonEvent>(deviceFilePath, 16, 1024) {

    constructor() : this("/dev/input/event0")

    var ClicksOnly = true

    override fun Parse(bytes: ByteArray, offset: Int): Optional<ButtonEvent> {
        if (bytes.size() < 16) return Optional.empty()

        val evType  = intFromTwoBytes(bytes[offset + 9], bytes[offset + 8])
        //println("evType:  $evType ")
        val evCode  = intFromTwoBytes(bytes[offset + 11], bytes[offset + 10])
        //println("evCode:  $evCode ")
        val evValue = ByteBuffer.wrap(bytes, offset + 12, 4).order(ByteOrder.LITTLE_ENDIAN).int

        when {
            evType == 1 && (evValue == 1 || !ClicksOnly) -> return Optional.of(ButtonEvent(evCode, evValue == 1))
            else                                         -> return Optional.empty()
        }
    }

    fun CheckPressing(button: ButtonEventCode) {
        val isPressed = false
        val semaphore = Semaphore(0) //TODO to monitor
        var result: ButtonEvent? = null

        thread {
            this.ToObservable().subscribe(object : Subscriber<ButtonEvent>() {
                override fun onNext(p0: ButtonEvent) {
                    result = p0
                    this.unsubscribe()
                    semaphore.release()
                }

                override fun onCompleted() = Unit

                override fun onError(p0: Throwable) = throw p0

            })
        }
    }

}