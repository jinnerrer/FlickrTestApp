package dev.ivandyagilev.flickrtestapp.FlickrSearchManager

import com.google.gson.Gson

import javax.inject.Inject

import dev.ivandyagilev.flickrtestapp.Interface.RetrofitService
import io.reactivex.Observable
import retrofit2.Retrofit

class SearchManager @Inject
constructor(private val mService: RetrofitService) {
    private val mManager: SearchManager? = null

    fun search(text: String, page: Int): Observable<PhotosInfo> {
        return mService.search("flickr.photos.search",
                API_KEY,
                "relevance",
                1,
                30,
                page,
                "photos",
                1,
                "json",
                1,
                "'$text'")
    }

    companion object {

        private val API_KEY = "64290a8220ce91bdd978e743a30a3170"
    }

}
