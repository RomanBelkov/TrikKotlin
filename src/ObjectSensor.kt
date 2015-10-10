import java.util.*

/**
 * Created by Roman Belkov on 10.10.15.
 */

class ObjectSensor(scriptPath: String, commandPath: String, sensorPath: String) : VideoSensor<VideoSensorOutput>(scriptPath, commandPath, sensorPath) {

    constructor(videoSource: VideoSource) : this(videoSource.ScriptPath(), "/run/object-sensor.in.fifo", "/run/object-sensor.out.fifo")

    override fun Parse(text: String): Optional<VideoSensorOutput> {
        val parsedText = text.split(" "). drop(1). filter { it != "" }
        when {
            parsedText[0] == "loc:" && parsedText.size() == 4 ->
                return Optional.of(VideoSensorOutput.ObjectLocation(parsedText[1], parsedText[2], parsedText[3]))
            parsedText[0] == "hsv:" && parsedText.size() == 7 ->
                return Optional.of(VideoSensorOutput.DetectTarget(parsedText[1], parsedText[2], parsedText[3], parsedText[4], parsedText[5], parsedText[6]))
            else -> {println("Object sensor parse error!"); return  Optional.empty<VideoSensorOutput>()}
        }
    }
}