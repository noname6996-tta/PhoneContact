package com.tta.phonebookapplication.utils

sealed class State<out T> {
    data object Loading : State<Nothing>()
    class Success<T>(private val _data: T) : State<T>() {
        val data: T = _data
            get() {
                hasBeenHandled = true
                return field
            }
        var hasBeenHandled = false
    }

    data class Failure(private val _throwable: Throwable) : State<Nothing>() {
        val throwable = _throwable
            get() {
                hasBeenHandled = true
                return field
            }
        var hasBeenHandled = false
    }
}