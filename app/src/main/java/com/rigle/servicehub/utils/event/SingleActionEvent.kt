package com.rigle.servicehub.utils.event

import androidx.annotation.MainThread

class SingleActionEvent<T> : SingleLiveEvent<T>() {

    @MainThread
    fun call(v: T) {
        value = v
    }
}