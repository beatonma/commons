package org.beatonma.commons.app.ui.motionlayout

import android.content.Context
import android.os.Build.VERSION
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import org.beatonma.commons.R
import kotlin.math.cos
import kotlin.math.sin

/**
 * Clone of [androidx.constraintlayout.helper.widget.Layer], with added support for changing
 * alpha of referenced views.
 */
class AlphaLayer @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintHelper(context, attrs, defStyleAttr) {

    private var container: ConstraintLayout? = null
    private var rotationCenterX: Float? = null
    private var rotationCenterY: Float? = null
    private var groupRotateAngle: Float? = null
    private var layerScaleX = 1.0f
    private var layerScaleY = 1.0f
    private var computedCenterX: Float? = null
    private var computedCenterY: Float? = null
    private var computedMaxX: Float? = null
    private var computedMaxY: Float? = null
    private var computedMinX: Float? = null
    private var computedMinY: Float? = null
    private var needBounds = true
    private var views: Array<View?>? = null
    private var shiftX = 0.0f
    private var shiftY = 0.0f
    private var applyVisibilityOnAttach = false
    private var applyElevationOnAttach = false

    private var layerAlpha: Float = 1.0F

    override fun setAlpha(alpha: Float) {
        layerAlpha = alpha
        transform()
    }

    override fun init(attrs: AttributeSet?) {
        super.init(attrs)
        mUseViewMeasure = false
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout)
            context.withStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout) {
                val n = a.indexCount
                for (i in 0 until n) {
                    val attr = a.getIndex(i)
                    when (attr) {
                        R.styleable.ConstraintLayout_Layout_android_visibility -> applyVisibilityOnAttach = true
                        R.styleable.ConstraintLayout_Layout_android_elevation -> applyElevationOnAttach = true
                    }
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        container = parent as ConstraintLayout
        if (applyVisibilityOnAttach || applyElevationOnAttach) {
            val visibility = this.visibility
            var elevation = 0.0F
            if (VERSION.SDK_INT >= 21) {
                this.elevation = elevation
            }
            for (i in 0 until mCount) {
                val id = mIds[i]
                val view = container?.getViewById(id) ?: continue
                if (applyVisibilityOnAttach) {
                    view.visibility = visibility
                }
                if (applyElevationOnAttach && elevation > 0.0f && VERSION.SDK_INT >= 21) {
                    view.translationZ = view.translationZ + elevation
                }
            }
        }
    }

    override fun updatePreDraw(container: ConstraintLayout) {
        this.container = container
        val rotate = this.rotation
        if (rotate == 0.0f) {
            if (groupRotateAngle != null) {
                groupRotateAngle = rotate
            }
        }
        else {
            groupRotateAngle = rotate
        }
    }

    override fun setRotation(angle: Float) {
        groupRotateAngle = angle
        transform()
    }

    override fun setScaleX(scaleX: Float) {
        layerScaleX = scaleX
        transform()
    }

    override fun setScaleY(scaleY: Float) {
        layerScaleY = scaleY
        transform()
    }

    override fun setPivotX(pivotX: Float) {
        rotationCenterX = pivotX
        transform()
    }

    override fun setPivotY(pivotY: Float) {
        rotationCenterY = pivotY
        transform()
    }

    override fun setTranslationX(dx: Float) {
        shiftX = dx
        transform()
    }

    override fun setTranslationY(dy: Float) {
        shiftY = dy
        transform()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        applyLayoutFeatures()
    }

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)
        applyLayoutFeatures()
    }

    override fun updatePostLayout(container: ConstraintLayout) {
        reCacheViews()
        computedCenterX = null
        computedCenterY = null
        val params = this.layoutParams as ConstraintLayout.LayoutParams
        val widget = params.constraintWidget
        widget.width = 0
        widget.height = 0
        calcCenters()
        val left = (computedMinX?.toInt() ?: 0) - this.paddingLeft
        val top = (computedMinY?.toInt() ?: 0) - this.paddingTop
        val right = (computedMaxX?.toInt() ?: 0) + this.paddingRight
        val bottom = (computedMaxY?.toInt() ?: 0) + this.paddingBottom
        layout(left, top, right, bottom)
        if (groupRotateAngle != null) {
            transform()
        }
    }

    private fun reCacheViews() {
        if (container != null && mCount != 0) {
            if (this.views == null || this.views!!.size != mCount) {
                this.views = arrayOfNulls(mCount)
            }
            for (i in 0 until mCount) {
                val id = mIds[i]
                this.views!![i] = container!!.getViewById(id)
            }
        }
    }

    protected fun calcCenters() {
        if (container != null) {
            if (needBounds || computedCenterX != null || computedCenterY != null) {
                if (rotationCenterX != null && rotationCenterY != null) {
                    computedCenterY = rotationCenterY
                    computedCenterX = rotationCenterX
                }
                else {
                    val views = getViews(container)
                    var minx = views[0].left
                    var miny = views[0].top
                    var maxx = views[0].right
                    var maxy = views[0].bottom
                    for (i in 0 until mCount) {
                        val view = views[i]
                        minx = minx.coerceAtMost(view.left)
                        miny = miny.coerceAtMost(view.top)
                        maxx = maxx.coerceAtLeast(view.right)
                        maxy = maxy.coerceAtLeast(view.bottom)
                    }
                    computedMaxX = maxx.toFloat()
                    computedMaxY = maxy.toFloat()
                    computedMinX = minx.toFloat()
                    computedMinY = miny.toFloat()
                    computedCenterX = when(rotationCenterX) {
                        null -> rotationCenterX
                        else -> ((minx + maxx) / 2).toFloat()
                    }
                    computedCenterY = when(rotationCenterY) {
                        null -> rotationCenterY
                        else -> ((miny + maxy) / 2).toFloat()
                    }
                }
            }
        }
    }

    private fun transform() {
        if (container != null) {
            if (this.views == null) {
                reCacheViews()
            }
            calcCenters()
            val rad = Math.toRadians((groupRotateAngle ?: 0).toDouble())
            val sin = sin(rad).toFloat()
            val cos = cos(rad).toFloat()
            val m11 = layerScaleX * cos
            val m12 = -layerScaleY * sin
            val m21 = layerScaleX * sin
            val m22 = layerScaleY * cos
            for (i in 0 until mCount) {
                val view = this.views?.get(i) ?: continue
                val x = (view.left + view.right) / 2
                val y = (view.top + view.bottom) / 2
                val dx = x.toFloat() - (computedCenterX ?: 0F)
                val dy = y.toFloat() - (computedCenterY ?: 0F)
                val shiftx = m11 * dx + m12 * dy - dx + shiftX
                val shifty = m21 * dx + m22 * dy - dy + shiftY
                view.apply {
                    translationX = shiftx
                    translationY = shifty
                    scaleY = scaleY
                    scaleX = scaleX
                    rotation = groupRotateAngle ?: 0F
                    alpha = layerAlpha
                }
            }
        }
    }
}
