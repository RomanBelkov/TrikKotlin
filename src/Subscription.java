/**
 * Subscription returns from {@link Observable#subscribe(Subscriber)} to allow unsubscribing.
 * <p>
 * See the utilities in {@link Subscriptions} and the implementations in the {@code rx.subscriptions} package.
 * <p>
 * This interface is the RxJava equivalent of {@code IDisposable} in Microsoft's Rx implementation.
 */
public interface Subscription {

    /**
     * Stops the receipt of notifications on the {@link Subscriber} that was registered when this Subscription
     * was received.
     * <p>
     * This allows unregistering an {@link Subscriber} before it has finished receiving all events (i.e. before
     * onCompleted is called).
     */
    void unsubscribe();

    /**
     * Indicates whether this {@code Subscription} is currently unsubscribed.
     *
     * @return {@code true} if this {@code Subscription} is currently unsubscribed, {@code false} otherwise
     */
    boolean isUnsubscribed();

}
