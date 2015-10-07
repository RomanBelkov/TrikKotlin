import java.io.Closeable

/**
 * Created by Roman Belkov on 25.09.15.
 */
public interface Observable<T> {
    fun Subscribe(observer : Observer<T>): Closeable
}

fun <T> Observable<T>.Subscribe(f: (T) -> Unit): Closeable = this.Subscribe(
    object : Observer<T> {
        override fun onError(e: Throwable) {
            return
        }

        override fun onCompleted() {
            return
        }

        override fun onNext(t: T) {
            return f(t)
        }
    })

/*let interval(timeSpan: System.TimeSpan) =
        let counter = ref 0
        let notifier = new Subject<int>()
        let cts = new CancellationTokenSource()

        let rec triggering (token: CancellationToken) =
            async {
                if not token.IsCancellationRequested then
                    notifier.OnNext !counter
                    incr counter
                    do! Async.Sleep(int timeSpan.Ticks/10000)
                    return! triggering token
                  }
        triggering cts.Token |> Async.Start
        notifier :> IObservable<_>
   */

//fun <T> Observable<T>.Interval() {}

private fun <A, B> protect(f: () -> A, succeed: (A) -> B, fail: (Throwable) -> B): B {
    try {
        val v = f()
        return succeed(v)
    } catch (e: Throwable) {
        return fail(e)
    }
}

fun <T, E> Observable<T>.Map(f: (T) -> E, observable: Observable<T>) = object : Observable<E> {
    override fun Subscribe(observer: Observer<E>): Closeable = object : BasicObserver<T>() {

        override fun Next(value: T) = protect({f(value)}, {x -> observer.onNext(x)}, {x -> observer.onError(x)})

        override fun Completed() = observer.onCompleted()

        override fun Error(e: Throwable) = observer.onError(e)

        override fun close() = Completed()

    }
}

