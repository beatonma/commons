package org.beatonma.commons.app.ui.views.chip

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.Interpolation
import org.beatonma.commons.databinding.ViewCollapsibleChipBinding
import org.beatonma.commons.kotlin.extensions.inflate

@Deprecated(
    message = """
Replace the RecyclerView with a [ScrollableChipContainer] which handles chip rendering directly
to canvas with a single View. Much more reliable animations and click handling!

[CollapsibleChipHolder] was an interesting experiment with MotionLayout but involved a lot of
view remeasuring which was always going to be janky.
"""
)
open class CollapsibleChipHolder(
    parent: ViewGroup
): RecyclerView.ViewHolder(
    parent.inflate(R.layout.view_collapsible_chip)
), CollapsibleChip {
    private var motionCollapsedWidth = Integer.MAX_VALUE
    private var motionExpandedWidth = Integer.MIN_VALUE
    private val interpolator = Interpolation.motion
    private var autoCollapseJob: Job? = null

    val vh = ViewCollapsibleChipBinding.bind(itemView)
    init {
        vh.chipIcon.setOnClickListener(getDefaultToggleClickListener())
        vh.cancelIcon.setOnClickListener { collapse() }

        vh.motion.setTransitionListener(object: MotionLayout.TransitionListener {
            private fun updateWidthLimits() {
                val w = itemView.measuredWidth
                motionCollapsedWidth = motionCollapsedWidth.coerceAtMost(w)
                motionExpandedWidth = motionExpandedWidth.coerceAtLeast(w)
            }

            override fun onTransitionTrigger(motion: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

            override fun onTransitionStarted(motion: MotionLayout?, startState: Int, endState: Int) {
                updateWidthLimits()
            }

            override fun onTransitionChange(motion: MotionLayout?, startState: Int, endState: Int, progress: Float) {
                val widthDelta = motionExpandedWidth - motionCollapsedWidth
                val progressWidth = when (widthDelta) {
                    0 -> {
                        (motionCollapsedWidth + interpolator.getInterpolation(progress) * (vh.background.measuredWidth - motionCollapsedWidth))
                    }
                    else -> {
                        (motionCollapsedWidth + interpolator.getInterpolation(progress) * widthDelta)
                    }
                }.toInt()
                itemView.updateLayoutParams { width = progressWidth }
            }

            override fun onTransitionCompleted(motion: MotionLayout?, p1: Int) {
                updateWidthLimits()
            }
        })
    }

    override fun bind(chipData: ChipData) {
        autoCollapseJob?.cancel()

        motionCollapsedWidth = Integer.MAX_VALUE
        motionExpandedWidth = Integer.MIN_VALUE
        vh.chipText.text = chipData.text
        vh.chipIcon.setImageResource(chipData.icon)
        if (chipData.iconTint != null) {
            vh.chipIcon.imageTintList = ColorStateList.valueOf(chipData.iconTint)
        }
        vh.background.setOnClickListener(chipData.action)
    }

    fun isCollapsed() = vh.motion.currentState == vh.motion.startState
    fun isExpanded() = vh.motion.currentState == vh.motion.endState

    override fun collapse() {
        autoCollapseJob?.cancel()
        vh.motion.transitionToStart()
    }

    override fun expand() {
        vh.motion.transitionToEnd()

        autoCollapseJob = (itemView.context as? LifecycleOwner)?.lifecycleScope?.launch {
            delay(CollapsibleChip.AUTO_COLLAPSE_TIME_MILLIS)
            collapse()
        }
    }

    private fun getDefaultToggleClickListener() = View.OnClickListener {
        with(vh.motion) {
            when (currentState) {
                startState -> expand()
                endState -> collapse()
            }
        }
    }
}
