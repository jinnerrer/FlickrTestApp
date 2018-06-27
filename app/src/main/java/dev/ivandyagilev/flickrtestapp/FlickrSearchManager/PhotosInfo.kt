package dev.ivandyagilev.flickrtestapp.FlickrSearchManager

import com.google.gson.annotations.SerializedName

class PhotosInfo {

    @SerializedName("photos")
    var photos: SearchResults? = null

}
