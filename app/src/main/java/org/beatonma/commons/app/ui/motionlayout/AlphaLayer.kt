package org.beatonma.commons.app.ui.motionlayout

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * [ConstraintHelper] for applying alpha values to all referenced Views
 */
class AlphaLayer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
): ConstraintHelper(context, attrs, defStyleAttr) {
    private var layerAlpha: Float = 1.0F

    override fun setAlpha(alpha: Float) {
        layerAlpha = alpha
        applyLayoutFeatures()
    }

    override fun applyLayoutFeatures(container: ConstraintLayout) {
        mIds.forEach { viewId ->
            val view = container.getViewById(viewId)
            if (view != null) {
                view.alpha = layerAlpha
            }
        }
    }
}
