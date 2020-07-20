package org.beatonma.commons.app.ui.views.chip

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.transition.TransitionManager
import org.beatonma.commons.R
import org.beatonma.commons.databinding.ViewCollapsibleChipBinding

private const val TAG = "CollapsibleChip"


@Deprecated("Prefer CollapsibleChipViewHolder")
class CollapsibleChipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    var motionStartWidth: Int = 0
    var motionEndWidth: Int = 0

    val binding: ViewCollapsibleChipBinding = ViewCollapsibleChipBinding.bind(
        LayoutInflater.from(context).inflate(R.layout.view_collapsible_chip, this, false)
    )

    init {
        addView(binding.root)

        binding.background.setOnClickListener(getDefaultToggleClickListener())
        binding.cancelIcon.setOnClickListener { collapse() }
    }
//
    private fun collapse()  {
        TransitionManager.beginDelayedTransition(this)
        binding.motion.transitionToStart()
    }
    private fun expand() {
        TransitionManager.beginDelayedTransition(this)
        binding.motion.transitionToEnd()
    }

    fun bind(action: ChipData) {
        binding.chipText.text = action.text
        binding.chipIcon.setImageResource(action.icon)
        if (action.iconTint != null) {
            binding.chipIcon.imageTintList = ColorStateList.valueOf(action.iconTint)
        }
//        if (action.backgroundTint != null) {
//          binding.motionLayout.backgroundTintList = ColorStateList.valueOf(tint)
//        }
        setOnClickListener(action.action)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        val wrappedListener = OnClickListener { view ->
            with(binding.motion) {
                Log.d(TAG, "$currentState, $startState, $endState")
                when (currentState) {
                    startState -> expand()
                    endState -> l?.onClick(view)
                }
            }
        }
        binding.motion.setOnClickListener(wrappedListener)
        binding.chipIcon.setOnClickListener(wrappedListener)
    }

    private fun getDefaultToggleClickListener() = OnClickListener {
        with(binding.motion) {
            when (currentState) {
                startState -> expand()
                endState -> collapse()
            }
        }
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//
//        with (binding.motion) {
//            motionStartWidth = getConstraintSet(startState).getWidth(R.id.background)
//            motionEndWidth = getConstraintSet(endState).getWidth(R.id.background)
//        }
//    }
//}
}
