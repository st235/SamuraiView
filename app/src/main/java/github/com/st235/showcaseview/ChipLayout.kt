package github.com.st235.showcaseview

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * Layout for positioning chip components as a group.
 */
class ChipLayout : ViewGroup {

    /**
     * Creates new one from xml with style from theme attribute
     * @param attrs an xml attributes set
     * @param defStyleAttr a style from theme
     */
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        setWillNotDraw(true)
    }

    /**
     * Creates new one from xml with a style from theme attribute or style resource
     * Api 21 and above
     * @param attrs an xml attributes set
     * @param defStyleAttr a style from theme
     * @param defStyleRes a style resource
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {

        setWillNotDraw(true)
    }

    /**
     * {@inheritDoc}
     */
    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LinearLayout.LayoutParams
    }

    /**
     * {@inheritDoc}
     */
    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return LinearLayout.LayoutParams(context, attrs)
    }

    /**
     * {@inheritDoc}
     */
    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return LinearLayout.LayoutParams(p)
    }

    /**
     * {@inheritDoc}
     */
    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * {@inheritDoc}
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val childCount = childCount
        val preMeasuredWidth = (View.MeasureSpec.getSize(widthMeasureSpec)
                - paddingLeft - paddingRight)

        var childState = 0

        var width = 0
        var height = 0

        var currentRowWidth = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val params = getChildLayoutParams(child)

            if (child.visibility == View.GONE) continue

            val horizontalMargins = params.leftMargin + params.rightMargin
            val verticalMargins = params.topMargin + params.bottomMargin
            measureChildWithMargins(
                child,
                widthMeasureSpec, 0, heightMeasureSpec, 0
            )

            width += Math.max(width, child.measuredWidth + horizontalMargins)
            currentRowWidth += child.measuredWidth + horizontalMargins

            if (currentRowWidth > preMeasuredWidth) {
                height += child.measuredHeight + verticalMargins
                currentRowWidth = child.measuredWidth + horizontalMargins
            } else {
                height = Math.max(height, child.measuredHeight + verticalMargins)
            }

            childState = View.combineMeasuredStates(childState, child.measuredState)
        }

        height = Math.max(height, suggestedMinimumHeight)
        width = Math.max(width, suggestedMinimumWidth)

        setMeasuredDimension(
            View.resolveSizeAndState(width, widthMeasureSpec, childState),
            View.resolveSizeAndState(
                height, heightMeasureSpec,
                childState shl View.MEASURED_HEIGHT_STATE_SHIFT
            )
        )
    }

    /**
     * {@inheritDoc}
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = childCount

        val layoutLeft = paddingLeft
        val layoutRight = measuredWidth - paddingRight
        val layoutTop = paddingTop
        val layoutBottom = measuredHeight - paddingBottom
        val layoutWidth = layoutRight - layoutLeft
        val layoutHeight = layoutTop - layoutBottom

        var maxHeight = 0

        var width: Int
        var height: Int
        var left = layoutLeft
        var top = layoutTop

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val params = getChildLayoutParams(child)

            if (child.visibility == View.GONE) continue

            child.measure(
                View.MeasureSpec.makeMeasureSpec(layoutWidth, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(layoutHeight, View.MeasureSpec.AT_MOST)
            )

            width = child.measuredWidth + params.leftMargin + params.rightMargin
            height = child.measuredHeight + params.topMargin + params.bottomMargin

            if (left + width >= layoutRight) {
                left = layoutLeft
                top += maxHeight
                maxHeight = 0
            }

            child.layout(
                left + params.leftMargin,
                top + params.topMargin,
                left + child.measuredWidth + params.leftMargin,
                top + child.measuredHeight + params.topMargin
            )

            if (maxHeight < height)
                maxHeight = height

            left += width
        }
    }

    /**
     * Returns layout params from child if exists or generates new one otherwise
     * @param child a view from this view group
     * @return layout params for child view
     */
    private fun getChildLayoutParams(child: View): LinearLayout.LayoutParams {
        val params = child.layoutParams
        return (if (checkLayoutParams(params))
            params
        else
            generateDefaultLayoutParams()) as LinearLayout.LayoutParams
    }
}
/**
 * Creates new one from code
 */
/**
 * Creates new one from xml
 * @param attrs an xml attributes set
 */
