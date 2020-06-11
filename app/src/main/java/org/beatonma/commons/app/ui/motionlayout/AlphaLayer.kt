package org.beatonma.commons.app.ui.motionlayout

import android.content.Context
import android.os.Build.VERSION
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.cos
import kotlin.math.sin

/**
 * Clone of [androidx.constraintlayout.helper.widget.Layer], with added support for changing
 * alpha of referenced views.
 */
class AlphaLayer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintHelper(context, attrs, defStyleAttr) {

    private var layerAlpha: Float = 1.0F

    override fun setAlpha(alpha: Float) {
        layerAlpha = alpha
        transform()
    }

    private var container: ConstraintLayout? = null
    private var rotationCenterX: Float? = null
    private var rotationCenterY: Float? = null
    private var groupRotateAngle: Float? = null
    private var layerScaleX: Float = 1.0F
    private var layerScaleY: Float = 1.0F
    private var computedCenterX: Float? = null
    private var computedCenterY: Float? = null
    private var computedMaxX: Float? = null
    private var computedMaxY: Float? = null
    private var computedMinX: Float? = null
    private var computedMinY: Float? = null
    private var needBounds = true
    private var views: Array<View?>? = null
    private var mShiftX = 0.0F
    private var mShiftY = 0.0F

    override fun init(attrs: AttributeSet?) {
        super.init(attrs)
        mUseViewMeasure = false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.container = this.parent as ConstraintLayout
    }

    override fun updatePreDraw(container: ConstraintLayout) {
        this.container = container
        val visibility = this.visibility
        val rotate = this.rotation
        if (rotate == 0.0f) {
            if (groupRotateAngle == null) groupRotateAngle = rotate
        }
        else {
            this.groupRotateAngle = rotate
        }
        var elevation = 0.0f
        if (VERSION.SDK_INT >= 21) {
            elevation = this.elevation
        }
        if (mReferenceIds != null) {
            setIds(mReferenceIds)
        }
        for (i in 0 until mCount) {
            val id = mIds[i]
            val view = container.getViewById(id)
            if (view != null) {
                view.visibility = visibility
                if (elevation > 0.0f && VERSION.SDK_INT >= 21) {
                    view.elevation = elevation
                }
            }
        }
    }

    override fun setRotation(angle: Float) {
        this.groupRotateAngle = angle
        transform()
    }

    override fun setScaleX(scaleX: Float) {
        this.layerScaleX = scaleX
        transform()
    }

    override fun setScaleY(scaleY: Float) {
        this.layerScaleY = scaleY
        transform()
    }

    override fun setPivotX(pivotX: Float) {
        this.rotationCenterX = pivotX
        transform()
    }

    override fun setPivotY(pivotY: Float) {
        this.rotationCenterY = pivotY
        transform()
    }

    override fun setTranslationX(dx: Float) {
        this.mShiftX = dx
        transform()
    }

    override fun setTranslationY(dy: Float) {
        this.mShiftY = dy
        transform()
    }

    // Not called by MotionLayout!
    override fun updatePostLayout(container: ConstraintLayout) {
        reCacheViews()
        this.computedCenterX = 0.0F
        this.computedCenterY = 0.0F
        val params = layoutParams as ConstraintLayout.LayoutParams
        val widget = params.constraintWidget
        widget.width = 0
        widget.height = 0
        calcCenters()

        val left = (computedMinX?.toInt() ?: 0) - paddingLeft
        val top = (computedMinY?.toInt() ?: 0) - paddingTop
        val right = (computedMaxX?.toInt() ?: 0) + paddingRight
        val bottom = (computedMaxY?.toInt() ?: 0) + paddingBottom
        layout(left, top, right, bottom)
        if (groupRotateAngle != null) {
            transform()
        }
    }

    private fun reCacheViews() {
        val container = this.container ?: return
        if (mCount != 0) {
            val localViews = when (views?.size) {
                mCount -> views!!
                else -> arrayOfNulls<View?>(mCount)
            }
            for (i in 0 until mCount) {
                val id = mIds[i]
                localViews[i] = container.getViewById(id)
            }
            this.views = localViews
        }
    }

    private fun calcCenters() {
        if (container != null) {
            if (
                needBounds || computedCenterX == null || computedCenterY == null
            ) {
                if (rotationCenterX != null && rotationCenterY != null) {
                    computedCenterY = rotationCenterY
                    computedCenterX = rotationCenterX
                }
                else {
                    val views = getViews(container)
                    var minX = views[0].left + paddingLeft
                    var minY = views[0].top + paddingTop
                    var maxX = views[0].right - paddingEnd
                    var maxY = views[0].bottom - paddingBottom
                    for (i in 0 until mCount) {
                        val view = views[i]
                        minX = minX.coerceAtMost(view.left + paddingLeft)
                        minY = minY.coerceAtMost(view.top + paddingTop)
                        maxX = maxX.coerceAtLeast(view.right - paddingEnd)
                        maxY = maxY.coerceAtLeast(view.bottom - paddingBottom)
                    }
                    computedMaxX = maxX.toFloat()
                    computedMaxY = maxY.toFloat()
                    computedMinX = minX.toFloat()
                    computedMinY = minY.toFloat()
                    computedCenterX = when (rotationCenterX) {
                        null -> ((minX + maxX) / 2F)
                        else -> rotationCenterX
                    }
                    computedCenterY = when (rotationCenterY) {
                        null -> ((minY + maxY) / 2F)
                        else -> rotationCenterY
                    }
                }
            }
        }
    }

    private fun transform() {
        if (container != null) {
            if (views == null) {
                reCacheViews()
            }
            calcCenters()
            val rad = Math.toRadians(groupRotateAngle?.toDouble() ?: 0.0)
            val sin = sin(rad).toFloat()
            val cos = cos(rad).toFloat()
            val m11 = layerScaleX * cos
            val m12 = -layerScaleY * sin
            val m21 = layerScaleX * sin
            val m22 = layerScaleY * cos
            for (i in 0 until mCount) {
                val view = views?.get(i) ?: continue
                val x = (view.left + view.right) / 2
                val y = (view.top + view.bottom) / 2
                val dx = x.toFloat() - (computedCenterX ?: 0F)
                val dy = y.toFloat() - (computedCenterY ?: 0F)
                val shiftx = m11 * dx + m12 * dy - dx + mShiftX
                val shifty = m21 * dx + m22 * dy - dy + mShiftY
                view.translationX = shiftx
                view.translationY = shifty
                view.scaleY = layerScaleY
                view.scaleX = layerScaleX
                view.rotation = groupRotateAngle ?: 0F
                view.alpha = layerAlpha
            }
        }
    }
}
