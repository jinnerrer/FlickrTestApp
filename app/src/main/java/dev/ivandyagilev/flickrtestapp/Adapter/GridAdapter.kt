package dev.ivandyagilev.flickrtestapp.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar

import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import dev.ivandyagilev.flickrtestapp.Activity.FullscreenActivity
import dev.ivandyagilev.flickrtestapp.Model.Photo
import dev.ivandyagilev.flickrtestapp.R
import android.support.v4.content.ContextCompat.startActivity
import android.app.ActivityOptions
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation


class GridAdapter @Inject
constructor(private val photoList: List<Photo>, private val glide: RequestManager) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {


    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val photo = photoList[position]

        holder.mView.setOnClickListener { view ->

            val intent = Intent(context, FullscreenActivity::class.java)
            intent.putExtra(IMAGE_URL, photo.geLargetUrl())
            intent.putExtra(IMAGE_TITLE, photo.title)
            intent.putExtra(IMAGE_SMALL_URL, photo.url)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, holder.mView as View, "row_transition")
            context!!.startActivity(intent,options.toBundle())
        }

        holder.setImage(photo.thumb, photo.url)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    inner class ViewHolder internal constructor(internal var mView: View) : RecyclerView.ViewHolder(mView) {

        @BindView(R.id.itemImageView)
        lateinit var mItemImage: ImageView

        @BindView(R.id.thumbView)
        lateinit var mThumbView: ImageView

        @BindView(R.id.progressBar)
        lateinit var mProgressBar: ProgressBar

        init {
            ButterKnife.bind(this, mView)

        }

        internal fun setImage(thumb: String, imageUrl: String) {

            mThumbView.setImageDrawable(null)
            mItemImage.setImageDrawable(null)

            mProgressBar.visibility = View.VISIBLE



            glide.load(thumb).into(mThumbView)
            glide.load(imageUrl).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    mProgressBar.visibility = View.GONE
                    return false
                }
            }).into(mItemImage)
        }
    }

    companion object {
        val IMAGE_URL = "image_url"
        val IMAGE_TITLE = "image_title"
        val IMAGE_SMALL_URL = "image_small_url"
    }

}
