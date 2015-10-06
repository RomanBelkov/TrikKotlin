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