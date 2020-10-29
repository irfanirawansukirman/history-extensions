package com.irfanirawansukirman.pipileman

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

// Informacion
// https://medium.com/androiddevelopers/unit-testing-livedata-and-other-common-observability-problems-bb477262eb04

@VisibleForTesting
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 3,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T? {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("Value was never set")
    }
    return data
}

@VisibleForTesting
fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
    val observer = OneTimeObserver(onChangeHandler)
    //Lifecycle owner and observer
    observe(observer, observer)
}

/**
 * Custom observer that observes once and is cleared once done
 */
internal class OneTimeObserver<T>(private val handler: (T) -> Unit) : Observer<T>, LifecycleOwner {

    private val lifecycle = LifecycleRegistry(this)

    init {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun getLifecycle(): Lifecycle = lifecycle

    override fun onChanged(t: T) {
        handler(t)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

}
