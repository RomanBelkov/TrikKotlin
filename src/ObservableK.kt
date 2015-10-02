import java.io.Closeable

/**
 * Created by Roman Belkov on 25.09.15.
 */
public interface ObservableK<T> {
    fun Subscribe(observer : Observer<T>): Closeable
}