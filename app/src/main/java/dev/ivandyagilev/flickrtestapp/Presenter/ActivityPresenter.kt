package dev.ivandyagilev.flickrtestapp.Presenter

import javax.inject.Inject

import dev.ivandyagilev.flickrtestapp.FlickrSearchManager.PhotosInfo
import dev.ivandyagilev.flickrtestapp.FlickrSearchManager.SearchManager
import dev.ivandyagilev.flickrtestapp.Interface.SearchActivityMvpView
import dev.ivandyagilev.flickrtestapp.Model.Photo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


class ActivityPresenter @Inject
constructor(private var photoList: MutableList<Photo>?, private var searchManager: SearchManager) : BasePresenter<SearchActivityMvpView>() {

    override fun attachView() {

    }

    fun loadImages(query: String, page: Int) {
        _loadImages(query, page)
    }

    private fun _loadImages(query: String, page: Int) {

        mvpView!!.startLoading()

        if (page == 1) {
            photoList!!.clear()
        }

        searchManager.search(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<PhotosInfo>() {

                    override fun onError(e: Throwable) {}

                    override fun onComplete() {

                    }

                    override fun onNext(photos: PhotosInfo) {

                        if (photos.photos!!.mList == null) {
                            photoList = mutableListOf<Photo>()
                            mvpView!!.noResults()
                        } else {
                            photoList!!.addAll(photos.photos!!.mList!!)
                            mvpView!!.stopLoading()
                        }

                    }
                })

    }

}
