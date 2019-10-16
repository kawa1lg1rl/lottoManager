package com.kawa1lg1rl.lotto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageHelper {

    fun getBitmap(name:String, size: Int) : Bitmap? {
        val options : BitmapFactory.Options = BitmapFactory.Options()
        options.inSampleSize = size

        var resources = App.context().resources

        return BitmapFactory.decodeResource(
            resources,
            resources.getIdentifier(name, "drawable", App.context().packageName),
            options
        )
    }
}