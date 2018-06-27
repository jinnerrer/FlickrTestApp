package dev.ivandyagilev.flickrtestapp.Activity

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast


import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dev.ivandyagilev.flickrtestapp.Adapter.GridAdapter.Companion.IMAGE_SMALL_URL
import dev.ivandyagilev.flickrtestapp.Adapter.GridAdapter.Companion.IMAGE_TITLE
import dev.ivandyagilev.flickrtestapp.Adapter.GridAdapter.Companion.IMAGE_URL
import dev.ivandyagilev.flickrtestapp.R


class FullscreenActivity : AppCompatActivity() {
    private val mHideHandler = Handler()


    @BindView(R.id.item_image)
    lateinit var mImageView: ImageView

    @BindView(R.id.image_title)
    lateinit var mImageTitle: TextView

    @BindView(R.id.progressBar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.fullscreen_content_controls)
    lateinit var mControlsView: LinearLayout

    private val mHidePart2Runnable = Runnable {
        mImageView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private val mShowPart2Runnable = Runnable {
        val actionBar = supportActionBar
        actionBar?.show()
        mControlsView.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = { hide() }

    @SuppressLint("ClickableViewAccessibility")
    private val mDelayHideTouchListener = View.OnTouchListener{ _, _->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        ButterKnife.bind(this)

        mVisible = true

        val url = intent.getStringExtra(IMAGE_URL)
        val title = intent.getStringExtra(IMAGE_TITLE)
        val smallUrl = intent.getStringExtra(IMAGE_SMALL_URL)

        if (title != null && !title.isEmpty()) {
            mImageTitle.text = title
        }

        Glide.with(this).load(url).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                Toast.makeText(this@FullscreenActivity, R.string.image_error, Toast.LENGTH_SHORT).show()
                finish()
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                transitionStart()
                mProgressBar.visibility = View.GONE
                return false
            }
        }).into(mImageView)


        mImageView.setOnClickListener { toggle() }


        mImageTitle.setOnTouchListener(mDelayHideTouchListener)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        val actionBar = supportActionBar
        actionBar?.hide()
        mControlsView.visibility = View.GONE
        mVisible = false

        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @SuppressLint("InlinedApi")
    private fun show() {
        mImageView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {

        private val AUTO_HIDE = false

        private val AUTO_HIDE_DELAY_MILLIS = 3000
        private val UI_ANIMATION_DELAY = 300
    }

    private fun transitionStart() {
        val fade = window.enterTransition
        fade.addListener(object : android.transition.Transition.TransitionListener {
            override fun onTransitionStart(transition: android.transition.Transition) {

            }

            override fun onTransitionEnd(transition: android.transition.Transition) {

                fade.removeListener(this)
            }

            override fun onTransitionCancel(transition: android.transition.Transition) {

            }

            override fun onTransitionPause(transition: android.transition.Transition) {

            }

            override fun onTransitionResume(transition: android.transition.Transition) {

            }
        })
    }
}
