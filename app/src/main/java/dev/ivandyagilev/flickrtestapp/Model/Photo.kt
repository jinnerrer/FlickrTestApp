package dev.ivandyagilev.flickrtestapp.Model

class Photo {

    var id: String? = null
    var secret: String? = null
    var server: Long = 0
    var farm: Int = 0
    var title: String? = null

    val thumb: String
        get() = ("http://farm" + farm + ".static.flickr.com/"
                + server + "/" + id + "_" + secret + "_t" + ".jpg")

    val url: String
        get() = ("http://farm" + farm + ".static.flickr.com/"
                + server + "/" + id + "_" + secret + "_z" + ".jpg")

    fun geLargetUrl(): String {
        return ("http://farm" + farm + ".static.flickr.com/"
                + server + "/" + id + "_" + secret + "_b" + ".jpg")
    }
}
