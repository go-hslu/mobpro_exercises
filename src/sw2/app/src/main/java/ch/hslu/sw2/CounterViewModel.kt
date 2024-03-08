package ch.hslu.sw2

import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {

    var counter: Int = 0
        get() = field
        private set

    fun inc() {
        counter++
    }
}