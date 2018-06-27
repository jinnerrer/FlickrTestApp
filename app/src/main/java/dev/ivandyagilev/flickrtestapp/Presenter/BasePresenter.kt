package dev.ivandyagilev.flickrtestapp.Presenter

import dev.ivandyagilev.flickrtestapp.Interface.MvpView
import dev.ivandyagilev.flickrtestapp.Interface.Presenter

open class BasePresenter<T : MvpView> : Presenter<T> {
    override fun attachView() {
    }

    var mvpView: T? = null
        private set

    val isViewAttached: Boolean
        get() = mvpView != null

    override fun attachView(mvpView: T) {

        this.mvpView = mvpView

    }

    override fun detachView() {
        mvpView = null
    }

    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    class MvpViewNotAttachedException internal constructor() : RuntimeException("Please call attachView(MvpView) " + "before requesting data to the presenter")
}
