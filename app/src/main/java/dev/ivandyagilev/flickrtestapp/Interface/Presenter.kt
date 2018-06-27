package dev.ivandyagilev.flickrtestapp.Interface


interface Presenter<V : MvpView> {

    fun attachView(mvpView: V)
    fun detachView()
    fun attachView()

}
