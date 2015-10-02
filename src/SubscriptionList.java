import java.util.*;

/**
 * Created by Roman Belkov on 25.09.15.
 */

public final class SubscriptionList implements Subscription {

    private LinkedList<Subscription> subscriptions;
    private volatile boolean unsubscribed;

    public SubscriptionList() {
    }

    public SubscriptionList(final Subscription... subscriptions) {
        this.subscriptions = new LinkedList<Subscription>(Arrays.asList(subscriptions));
    }

    public SubscriptionList(Subscription s) {
        this.subscriptions = new LinkedList<Subscription>();
        this.subscriptions.add(s);
    }

    @Override
    public boolean isUnsubscribed() {
        return unsubscribed;
    }

    /**
     * Adds a new {@link Subscription} to this {@code SubscriptionList} if the {@code SubscriptionList} is
     * not yet unsubscribed. If the {@code SubscriptionList} <em>is</em> unsubscribed, {@code add} will
     * indicate this by explicitly unsubscribing the new {@code Subscription} as well.
     *
     * @param s
     *          the {@link Subscription} to add
     */
    public void add(final Subscription s) {
        if (s.isUnsubscribed()) {
            return;
        }
        if (!unsubscribed) {
            synchronized (this) {
                if (!unsubscribed) {
                    LinkedList<Subscription> subs = subscriptions;
                    if (subs == null) {
                        subs = new LinkedList<Subscription>();
                        subscriptions = subs;
                    }
                    subs.add(s);
                    return;
                }
            }
        }
        // call after leaving the synchronized block so we're not holding a lock while executing this
        s.unsubscribe();
    }

    public void remove(final Subscription s) {
        if (!unsubscribed) {
            boolean unsubscribe = false;
            synchronized (this) {
                LinkedList<Subscription> subs = subscriptions;
                if (unsubscribed || subs == null) {
                    return;
                }
                unsubscribe = subs.remove(s);
            }
            if (unsubscribe) {
                // if we removed successfully we then need to call unsubscribe on it (outside of the lock)
                s.unsubscribe();
            }
        }
    }

    /**
     * Unsubscribe from all of the subscriptions in the list, which stops the receipt of notifications on
     * the associated {@code Subscriber}.
     */
    @Override
    public void unsubscribe() {
        if (!unsubscribed) {
            List<Subscription> list;
            synchronized (this) {
                if (unsubscribed) {
                    return;
                }
                unsubscribed = true;
                list = subscriptions;
                subscriptions = null;
            }
            // we will only get here once
            unsubscribeFromAll(list);
        }
    }

    private static void unsubscribeFromAll(Collection<Subscription> subscriptions) {
        if (subscriptions == null) {
            return;
        }
        List<Throwable> es = null;
        for (Subscription s : subscriptions) {
            try {
                s.unsubscribe();
            } catch (Throwable e) {
                if (es == null) {
                    es = new ArrayList<Throwable>();
                }
                es.add(e);
            }
        }
        //Exceptions.throwIfAny(es); TODO HANDLE EXCEPTIONS SOMEHOW
    }
    /* perf support */
    public void clear() {
        if (!unsubscribed) {
            List<Subscription> list;
            synchronized (this) {
                list = subscriptions;
                subscriptions = null;
            }
            unsubscribeFromAll(list);
        }
    }
    /**
     * Returns true if this composite is not unsubscribed and contains subscriptions.
     * @return {@code true} if this composite is not unsubscribed and contains subscriptions.
     */
    public boolean hasSubscriptions() {
        if (!unsubscribed) {
            synchronized (this) {
                return !unsubscribed && subscriptions != null && !subscriptions.isEmpty();
            }
        }
        return false;
    }
}
