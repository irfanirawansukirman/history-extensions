package com.irfanirawansukirman.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.irfanirawansukirman.extensions.util.SingleLiveEvent

inline fun <T> LiveData<T>.subscribe(owner: LifecycleOwner, crossinline onDataReceived: (T) -> Unit) =
    observe(owner, Observer { onDataReceived(it) })

// reference : https://medium.com/@riz_maulana/boost-your-android-development-process-with-these-awesome-kotlin-extensions-4f26bf2f7521
/*Old way to observe live data*/
// viewModel.newProfilePicture.observe(this, Observer { string ->
//Do something here
// })

/*Observe live data with extension*/
// observe(viewModel.newProfilePicture){ string ->
//Do something here
// }

/*Observe live data with extention + dedicated function
It will make code cleaner when you have long code to handle variable of live data
*/
// observe(viewModel.newProfilePicture, ::showProfilePicture)

// ...

// private fun showProfilePicture(url: String){
//Do something here
// }
fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

fun <T> LifecycleOwner.observe(liveData: MutableLiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

fun <T> LifecycleOwner.observe(liveData: SingleLiveEvent<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}