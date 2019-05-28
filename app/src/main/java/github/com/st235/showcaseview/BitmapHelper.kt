package github.com.st235.showcaseview

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.CheckResult
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange

object BitmapHelper {

    @CheckResult
    fun decodeSampledBitmapFromResource(
        res: Resources,
        @DrawableRes resId: Int,
        @IntRange(from = 0) reqWidth: Int,
        @IntRange(from = 0) reqHeight: Int
    ): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    @CheckResult
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        @IntRange(from = 0) reqWidth: Int,
        @IntRange(from = 0) reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}
