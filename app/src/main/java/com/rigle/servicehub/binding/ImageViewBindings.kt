package com.rigle.servicehub.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rigle.servicehub.R
import com.rigle.servicehub.injection.GlideApp


object ImageViewBindings {

    @JvmStatic
    @BindingAdapter(value = ["remote_image", "placeholder", "crop"], requireAll = false)
    fun remoteImage(
        imageView: ImageView,
        url: String?,
        placeholder: Drawable?,
        crop: Boolean = true,
    ) {
        val requestOptions = GlideApp.with(imageView.context).load(url.toString())
        if (placeholder != null) {
            requestOptions.placeholder(placeholder)
        } else {
            requestOptions.placeholder(R.drawable.no_image)
        }
        if (crop) {
            requestOptions.centerCrop()
        }
        requestOptions.into(imageView)
    }


    @JvmStatic
    @BindingAdapter(value = ["simpleResourse"])
    fun setStepGroupIcon(imageView: ImageView, simpleResourse: Int) {
        if (simpleResourse != -1) {
            imageView.setImageResource(simpleResourse)
        }
    }
}