package com.rigle.servicehub.ui.base

import android.view.View
import androidx.lifecycle.ViewModel
import com.rigle.servicehub.utils.event.SingleActionEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {
    open val TAG: String = this.javaClass.simpleName
    var compositeDisposable = CompositeDisposable()

    val onClick: SingleActionEvent<View> = SingleActionEvent()


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun Disposable.addToCompositeDisposable() {
        compositeDisposable.add(this)
    }

    open fun onClick(view: View) {
        onClick.value = view
    }

}