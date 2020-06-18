package org.beatonma.commons.app.ui.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import org.beatonma.commons.R

/**
 * A simple wrapper layout to allow a custom loading view to be used for an entire project
 * rather than setting the layout on each recyclerview adapter
 *
 *
 * Example usage:
 * <(app style definition)>
 *   <item name="BmaLoadingStyle">@style/BmaLoading</item>
 * </(app style definition)>
 * ...
 * <style name="BmaLoading">
 *   <item name="loading_layout">@layout/custom_loader</item>
 * </style>
 */
class CustomLayoutLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.RecyclerViewLoadingStyle,
    defStyleRes: Int = R.style.DefaultRecyclerViewLoadingStyle
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        gravity = Gravity.CENTER
        val layoutID: Int
        context.obtainStyledAttributes(attrs, R.styleable.CustomLayoutLoadingView, defStyleAttr, defStyleRes).apply {
            layoutID = getResourceId(R.styleable.CustomLayoutLoadingView_loading_layout, R.layout.vh_loading)
        }.recycle()

        inflate(context, layoutID, this)

        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
