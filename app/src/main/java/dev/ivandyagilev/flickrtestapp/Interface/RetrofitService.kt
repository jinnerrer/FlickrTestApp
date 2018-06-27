package dev.ivandyagilev.flickrtestapp.Interface


import dev.ivandyagilev.flickrtestapp.FlickrSearchManager.PhotosInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("/services/rest/")
    fun search(@Query("method") method: String,
               @Query("api_key") key: String,
               @Query("sort") sort: String,
               @Query("content_type") contentType: Int,
               @Query("per_page") perpage: Int,
               @Query("page") page: Int,
               @Query("media") media: String,
               @Query("has_geo") hasGeo: Int,
               @Query("format") format: String,
               @Query("nojsoncallback") num: Int,
               @Query("text") query: String): Observable<PhotosInfo>


}
