package org.beatonma.commons.kotlin.extensions

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import androidx.annotation.RequiresApi
import androidx.core.graphics.Insets
import androidx.core.view.*
import org.beatonma.commons.device.Sdk

/**
 * Call on root view.
 */
fun View.registerInsets(
    fromStatusBar: List<View>,
    fromNavBar: List<View>,
) {
    fromStatusBar.forEach { it.insetFromStatusBar() }
    fromNavBar.forEach { it.insetFromNavBar() }

    setOnApplyWindowInsetsListener { v, insets ->
        fromStatusBar.forEach { it.onApplyWindowInsets(insets) }
        fromNavBar.forEach { it.onApplyWindowInsets(insets) }
        insets
    }
    requestApplyInsetsWhenAttached()
}

fun View.registerInset(
    view: View,
    block: (v: View, insets: WindowInsetsCompat, padding: InitialPadding) -> Unit
) {
    view.doOnApplyWindowInsets { v, insets, padding ->
        block(v, insets, padding)
    }

    setOnApplyWindowInsetsListener { _, insets ->
        view.onApplyWindowInsets(insets)
        insets
    }

    requestApplyInsetsWhenAttached()
}

private fun View.insetFromStatusBar() =
    doOnApplyWindowInsets { view, insets, padding ->
        view.updatePadding(
            top = padding.top + insets.systemBarInsets.top,
        )
    }

private fun View.insetFromNavBar() =
    doOnApplyWindowInsets { view, insets, padding ->
        view.updatePadding(
            bottom = padding.bottom + insets.systemBarInsets.bottom,
        )
    }

val WindowInsetsCompat.systemBarInsets: Insets get() = getInsets(WindowInsetsCompat.Type.systemBars())


fun View.doOnApplyWindowInsets(f: (View, insets: WindowInsetsCompat, padding: InitialPadding) -> Unit) {
    val initialPadding = recordInitialPaddingForView(this)

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialPadding)
        insets
    }
    requestApplyInsetsWhenAttached()
}

data class InitialPadding internal constructor(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) =
    InitialPadding(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)


private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}


/**
 * Callbacks not being called? Try again with next release
 */
fun View.addImeWindowInsetsAnimation() {
    if (!Sdk.isR) {
        Log.w(autotag, "IME animations not supported by sdk=${Sdk.version} (${Build.VERSION_CODES.R} required)")
        return
    }

    Log.i(autotag, "Adding IME animation callback")
    val bottomMargin = this.marginBottom

    (context as? Activity)?.window?.setDecorFitsSystemWindows(false)
    setWindowInsetsAnimationCallback(
        @RequiresApi(Build.VERSION_CODES.R)
        object: WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
            override fun onProgress(
                insets: WindowInsets,
                animations: MutableList<WindowInsetsAnimation>,
            ): WindowInsets {
                updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    val margin = bottomMargin + insets.getInsets(WindowInsets.Type.ime()).bottom
                    updateMargins(bottom = margin.dump("Margin"))
                }
                return insets
            }
        }
    )
}
