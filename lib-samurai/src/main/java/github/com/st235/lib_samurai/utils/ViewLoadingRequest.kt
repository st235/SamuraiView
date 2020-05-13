package github.com.st235.lib_samurai.utils

import android.view.View
import android.view.ViewTreeObserver

class ViewLoadingRequest<V: View>(
    private val view: V,
    private val onViewReady: (view: View) -> Unit
): View.OnAttachStateChangeListener, ViewTreeObserver.OnPreDrawListener {

    init {
        view.addOnAttachStateChangeListener(this)

        if (view.windowToken != null) {
            onViewAttachedToWindow(view)
        }
    }

    override fun onViewAttachedToWindow(p0: View?) {
        view.viewTreeObserver.addOnPreDrawListener(this)
    }

    override fun onViewDetachedFromWindow(p0: View?) {
        view.viewTreeObserver.removeOnPreDrawListener(this)
    }

    override fun onPreDraw(): Boolean {
        val viewTreeObserver = view.viewTreeObserver

        if (!viewTreeObserver.isAlive) {
            return true
        }

        if (view.width <= 0 || view.height <= 0) {
            return true
        }

        view.removeOnAttachStateChangeListener(this)
        viewTreeObserver.removeOnPreDrawListener(this)

        onViewReady(view)

        return true
    }
}