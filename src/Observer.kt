/**
 * Created by Roman Belkov on 06.10.15.
 */

interface Observer<T> {

    fun onCompleted()


    fun onError(e: Throwable)


    fun onNext(t: T)

}