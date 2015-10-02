/**
 * Created by Roman Belkov on 25.09.15.
 */


public interface Producer {

    /**
     * Request a certain maximum number of items from this Producer. This is a way of requesting backpressure.
     * To disable backpressure, pass {@code Long.MAX_VALUE} to this method.
     * <p>
     * Requests are additive but if a sequence of requests totals more than {@code Long.MAX_VALUE} then
     * {@code Long.MAX_VALUE} requests will be actioned and the extras <i>may</i> be ignored. Arriving at
     * {@code Long.MAX_VALUE} by addition of requests cannot be assumed to disable backpressure. For example,
     * the code below may result in {@code Long.MAX_VALUE} requests being actioned only.
     *
     * <pre>
     * request(100);
     * request(Long.MAX_VALUE-1);
     * </pre>
     *
     * @param n the maximum number of items you want this Producer to produce, or {@code Long.MAX_VALUE} if you
     *          want the Producer to produce items at its own pace
     */
    void request(long n);

}
