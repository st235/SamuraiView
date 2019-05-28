package github.com.st235.showcaseview

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.Dimension
import androidx.annotation.Px

import androidx.annotation.Dimension.DP

object Dimens {

    @Px
    fun dpToPx(@Dimension(unit = DP) dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }
}