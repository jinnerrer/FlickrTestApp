package dev.ivandyagilev.flickrtestapp.FlickrSearchManager

import com.google.gson.annotations.SerializedName
import dev.ivandyagilev.flickrtestapp.Model.Photo

class SearchResults {

    @SerializedName("photo")
    var mList: List<Photo>? = null

}
