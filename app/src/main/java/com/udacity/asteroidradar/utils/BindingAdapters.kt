package com.udacity.asteroidradar.utils

import android.content.res.Resources
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import kotlinx.coroutines.CoroutineScope

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.contentDescription =
            imageView.resources.getString(R.string.potentially_hazardous_asteroid_image)
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.contentDescription =
            imageView.resources.getString(R.string.not_hazardous_asteroid_image)
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.contentDescription =
            imageView.resources.getString(R.string.potentially_hazardous_asteroid_image)
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.contentDescription =
            imageView.resources.getString(R.string.not_hazardous_asteroid_image)
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

