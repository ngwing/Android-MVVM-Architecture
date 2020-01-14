package com.anthony.foodmap.util

/**
 * Extension functions and Binding Adapters.
 */

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.anthony.foodmap.Event
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar


/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                EspressoIdlingResource.increment()
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                EspressoIdlingResource.decrement()
            }
        })
        show()
    }
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(context.getString(it), timeLength)
        }
    })
}

fun LatLng.toParamString():String{
    return StringBuilder().append(latitude).append(",").append(longitude).toString()
}

fun Int.toBitmapDescriptor(res:Resources):BitmapDescriptor? {
    return vectorToBitmap(this, null, res)
}

fun Int.toBitmapDescriptor(res:Resources, @ColorInt color: Int?):BitmapDescriptor? {
    return vectorToBitmap(this, color, res)
}

fun Int.color(res:Resources):Int{
    return res.getColor(this)
}

fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int?, res:Resources): BitmapDescriptor? {
    val vectorDrawable = ResourcesCompat.getDrawable(res, id, null)
    val bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
    color?.let {
        DrawableCompat.setTint(vectorDrawable, it)
    }
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun LatLng.distanceTo(point: LatLng): Float {
    val results = FloatArray(1)
    Location.distanceBetween(latitude, longitude,
            point.latitude, point.longitude,
            results)
    return results[0]
}