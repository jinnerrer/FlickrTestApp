package dev.ivandyagilev.flickrtestapp.Inject

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.gson.GsonBuilder

import dagger.Module
import dagger.Provides
import dev.ivandyagilev.flickrtestapp.Adapter.GridAdapter
import dev.ivandyagilev.flickrtestapp.FlickrSearchManager.SearchManager
import dev.ivandyagilev.flickrtestapp.Interface.RetrofitService
import dev.ivandyagilev.flickrtestapp.Model.Photo
import dev.ivandyagilev.flickrtestapp.Presenter.ActivityPresenter
import dev.ivandyagilev.flickrtestapp.View.LoadingDialog
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory



@Module
open class LogicModule(private val context: Context, private val photoList: MutableList<Photo>) {

    private val BASE_URL = "https://api.flickr.com"

    @Provides
    fun provideActivityPresenter(): ActivityPresenter {
        return ActivityPresenter(photoList, searchManager())
    }

    @Provides
    fun providesGridAdapter(): GridAdapter {
        return GridAdapter(photoList, glide(context))
    }

    @Provides
    fun providesLoadingDialog(): LoadingDialog {
        return LoadingDialog(context)
    }

    @Provides
    fun searchManager(): SearchManager {

        val gson = GsonBuilder()
                .setLenient()
                .create()

        val gsonConverterFactory = GsonConverterFactory.create(gson)

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val mService = retrofit.create(RetrofitService::class.java)

        return SearchManager(mService)
    }

    @Provides
    fun glide(context: Context): RequestManager {
        return Glide.with(context)
    }
}
