package dev.ivandyagilev.flickrtestapp.View

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import dev.ivandyagilev.flickrtestapp.R

class LoadingDialog(context: Context) : Dialog(context) {


    init {
        setCancelable(false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.progress_dialog)

        window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }

}
